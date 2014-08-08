package com.bluepowermod.part.cable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.part.cable.bluestone.ICableConnect;
import com.bluepowermod.util.Dependencies;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

/**
 * @author amadornes
 * 
 */
public abstract class CableWall extends BPPartFace {

    protected Object[] connections = new Object[6];
    protected Vector3 loc;

    /**
     * @author amadornes
     * 
     */
    public CableWall() {

    }

    /**
     * @author amadornes
     * 
     */
    public boolean canConnectOnSide(ForgeDirection direction) {

        return direction != ForgeDirection.getOrientation(getFace()) && direction != ForgeDirection.getOrientation(getFace()).getOpposite();
    }

    public abstract boolean canConnectToCable(CableWall cable);

    public abstract boolean canConnectToBlock(Block block, Vector3 location);

    public abstract boolean canConnectToTileEntity(TileEntity tile);

    /**
     * @author amadornes
     * 
     */
    public boolean isConnectedOnSide(ForgeDirection direction) {

        return connections[ForgeDirectionUtils.getSide(direction)] != null;
    }

    /**
     * @author amadornes
     * 
     */
    @Override
    public void onPartChanged() {

        onUpdate();
    }

    /**
     * @author amadornes
     * 
     */
    @Override
    public void onNeighborUpdate() {

        onUpdate();
    }

    /**
     * @author amadornes
     * 
     */
    @Override
    public void onNeighborTileUpdate() {

        onUpdate();
    }

    /**
     * @author amadornes
     * 
     */
    @Override
    public void onFirstTick() {

        onUpdate();
    }

    private boolean updating = false;
    private boolean updated = false;

    /**
     * @author amadornes
     * 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void onUpdate() {

        updating = true;

        if (loc == null || loc.getBlockX() != getX() || loc.getBlockY() != getY() || loc.getBlockZ() != getZ() || loc.getWorld() != getWorld())
            loc = new Vector3(getX(), getY(), getZ(), getWorld());

        if (getWorld() == null || getWorld().isRemote)
            return;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (!canConnectOnSide(d))
                continue;

            Vector3 vec = loc.getRelative(d);

            Object o = null;

            CableWall c = getCableOnSide(d);// Check connection to cables
            if (canConnectToCable(c)) {
                o = c;
            }
            if (o == null) {
                if (Loader.isModLoaded(Dependencies.FMP)) {
                    o = getPartOnSide(d);
                }
                if (o == null) {
                    if (vec.getBlock(true) != null && canConnectToBlock(vec.getBlock(), vec)) {// Check connection to blocks
                        o = vec;
                    } else if (vec.getTileEntity() != null && canConnectToTileEntity(vec.getTileEntity())) {// Check connection to TEs
                        o = vec;
                    }
                }
            }
            Object o2 = connections[ForgeDirectionUtils.getSide(d)];
            if (o2 != null && o2 != o) {
                if (o2 instanceof ICableConnect)
                    ((ICableConnect) o2).onDisconnect(this, d);
                if (o2 instanceof CableWall)
                    ((CableWall) o2).updated = true;
                onDisconnect(o2);
                updated = true;
            }

            connections[ForgeDirectionUtils.getSide(d)] = o;

            if (o != null && o2 != o) {
                if (o instanceof ICableConnect)
                    ((ICableConnect) o).onConnect(this, d);
                if (o instanceof CableWall)
                    ((CableWall) o).updated = true;
                onConnect(o);
                updated = true;
            }
        }

        updating = false;

        sendUpdatePacket();
        markPartForRenderUpdate();
    }

    /**
     * @author amadornes
     * 
     */
    private CableWall getCableOnSide(ForgeDirection dir) {

        Vector3 vec = loc.getRelative(dir);
        IMultipartCompat compat = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP));

        // Check for cables next to this one
        List<CableWall> l = compat.getBPParts(vec.getTileEntity(), CableWall.class);
        for (CableWall c : l) {
            if (c.getFace() == getFace()) {
                if (!compat.checkOcclusion(vec.getTileEntity(), getStripHitboxForSide(dir)))
                    return c;
            }
        }
        l.clear();

        // Check for cables in the same block
        l = compat.getBPParts(loc.getTileEntity(), CableWall.class);
        ForgeDirection dir2 = dir;
        if (dir2 == ForgeDirection.UP || dir2 == ForgeDirection.DOWN)
            dir2 = dir2.getOpposite();
        for (CableWall c : l) {
            if (ForgeDirection.getOrientation(c.getFace()) == dir2)
                return c;
        }
        l.clear();

        // Check for cables around corners
        ForgeDirection f = ForgeDirection.getOrientation(getFace());
        if (f == ForgeDirection.UP || f == ForgeDirection.DOWN)
            f = f.getOpposite();
        Vector3 vec2 = vec.getRelative(f);
        l = compat.getBPParts(vec2.getTileEntity(), CableWall.class);
        for (CableWall c : l) {
            ForgeDirection d = dir;
            if (d != ForgeDirection.UP && d != ForgeDirection.DOWN)
                d = d.getOpposite();
            if (ForgeDirection.getOrientation(c.getFace()) == d) {
                if (!c.updating)
                    c.onUpdate();
                return c;
            }
        }
        l.clear();

        return null;
    }

    private AxisAlignedBB getStripHitboxForSide(ForgeDirection dir) {

        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    /**
     * @author amadornes
     * 
     */
    @Optional.Method(modid = Dependencies.FMP)
    protected TMultiPart getPartOnSide(ForgeDirection dir) {

        return null;
    }

    /**
     * @author amadornes
     * 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onRemoved() {

        if (getWorld().isRemote)
            return;

        for (int i = 0; i < 6; i++) {
            Object o = connections[i];
            if (o instanceof ICableConnect) {
                ((ICableConnect) o).onDisconnect(this, ForgeDirection.getOrientation(i));
            } else if (o instanceof CableWall) {
                ((CableWall) o).onUpdate();// This method automatically calls onDisconnect :P
            }
        }
    }

    /**
     * @author amadornes
     * 
     */
    public void onConnect(Object o) {

    }

    /**
     * @author amadornes
     * 
     */
    public void onDisconnect(Object o) {

    }

    @Override
    public void update() {

        super.update();

        if (loc == null || loc.getBlockX() != getX() || loc.getBlockY() != getY() || loc.getBlockZ() != getZ() || loc.getWorld() != getWorld())
            loc = new Vector3(getX(), getY(), getZ(), getWorld());

        if (updated) {
            updated = false;
            sendUpdatePacket();
        }
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Vector3 v = getConnectionVector(d);
            tag.setString("con_" + d.name(), v == null ? "" : v.toString());
        }
    }

    private Vector3 getConnectionVector(ForgeDirection d) {

        Object o = connections[ForgeDirectionUtils.getSide(d)];
        if (o != null) {
            if (o instanceof Vector3)
                return (Vector3) o;
            if (o instanceof CableWall)
                return ((CableWall) o).loc;
            if (Loader.isModLoaded(Dependencies.FMP))
                return getConnectionVectorFMP(d);
        }
        return null;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private Vector3 getConnectionVectorFMP(ForgeDirection d) {

        Object o = connections[ForgeDirectionUtils.getSide(d)];
        if (o instanceof TMultiPart)
            return new Vector3(((TMultiPart) o).tile());
        return null;
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            connections[ForgeDirectionUtils.getSide(d)] = Vector3.fromString(tag.getString("con_" + d.name()));
    }
}
