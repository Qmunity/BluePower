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
        for (CableWall c : l)
            if (c.getWorld() != null)
                if (c.getFace() == getFace())
                    if (!compat.isOccupied(loc.getTileEntity(), getStripHitboxForSide(ForgeDirection.getOrientation(getFace()), dir))
                            && !compat.isOccupied(vec.getTileEntity(),
                                    getStripHitboxForSide(ForgeDirection.getOrientation(getFace()), dir.getOpposite())))
                        if (c.hasSetFace())
                            return c;
        l.clear();

        // Check for cables in the same block
        l = compat.getBPParts(loc.getTileEntity(), CableWall.class);
        ForgeDirection dir2 = dir;
        for (CableWall c : l) {
            if (c.getWorld() != null)
                if (ForgeDirection.getOrientation(c.getFace()) == dir2)
                    if (!compat.isOccupied(loc.getTileEntity(), getStripHitboxForSide(ForgeDirection.getOrientation(getFace()), dir)))
                        if (c.hasSetFace())
                            return c;
        }
        l.clear();

        // Check for cables around corners
        ForgeDirection f = ForgeDirection.getOrientation(getFace());
        Vector3 vec2 = vec.getRelative(f);
        l = compat.getBPParts(vec2.getTileEntity(), CableWall.class);
        for (CableWall c : l) {
            if (c.getWorld() != null) {
                ForgeDirection d = dir;
                if (ForgeDirection.getOrientation(c.getFace()).getOpposite() == d) {
                    boolean isOccluded = compat.isOccupied(loc.getTileEntity(), getStripHitboxForSide(ForgeDirection.getOrientation(getFace()), dir))
                            || compat.isOccupied(vec.getTileEntity(),
                                    getStripHitboxForSide(ForgeDirection.getOrientation(getFace()), dir.getOpposite()));
                    if (vec.getBlock() != null && vec.getBlock().isOpaqueCube())
                        isOccluded = true;
                    if (c.hasSetFace()) {
                        if (!c.updating)
                            c.onUpdate();
                        if (!isOccluded) {
                            return c;
                        }
                    }
                }
            }
        }
        l.clear();

        return null;
    }

    protected static AxisAlignedBB getStripHitboxForSide(ForgeDirection face, ForgeDirection dir) {

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

        double one = 1;
        double zer = 0;
        double min = 1 / 8D;
        double max = 7 / 8D;

        switch (face) {
        case UP:
            switch (dir) {
            case EAST:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, zer, one, min, one);
                break;
            case WEST:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, min, min, one);
                break;
            case NORTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, one, min, min);
                break;
            case SOUTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, max, one, min, one);
                break;
            default:
                break;
            }
            break;
        case DOWN:
            switch (dir) {
            case EAST:
                aabb = AxisAlignedBB.getBoundingBox(max, max, zer, one, one, one);
                break;
            case WEST:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, zer, min, one, one);
                break;
            case NORTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, zer, one, one, min);
                break;
            case SOUTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, max, one, one, one);
                break;
            default:
                break;
            }
            break;
        case EAST:
            switch (dir) {
            case UP:
                aabb = AxisAlignedBB.getBoundingBox(max, max, zer, one, one, one);
                break;
            case DOWN:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, zer, one, min, one);
                break;
            case NORTH:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, zer, one, one, min);
                break;
            case SOUTH:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, max, one, one, one);
                break;
            default:
                break;
            }
            break;
        case WEST:
            switch (dir) {
            case UP:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, zer, min, one, one);
                break;
            case DOWN:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, min, min, one);
                break;
            case NORTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, min, one, min);
                break;
            case SOUTH:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, max, min, one, one);
                break;
            default:
                break;
            }
            break;
        case NORTH:
            switch (dir) {
            case UP:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, zer, one, one, min);
                break;
            case DOWN:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, one, min, min);
                break;
            case EAST:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, zer, one, one, min);
                break;
            case WEST:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, zer, min, one, min);
                break;
            default:
                break;
            }
            break;
        case SOUTH:
            switch (dir) {
            case UP:
                aabb = AxisAlignedBB.getBoundingBox(zer, max, max, one, one, one);
                break;
            case DOWN:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, max, one, min, one);
                break;
            case EAST:
                aabb = AxisAlignedBB.getBoundingBox(max, zer, max, one, one, one);
                break;
            case WEST:
                aabb = AxisAlignedBB.getBoundingBox(zer, zer, max, min, one, one);
                break;
            default:
                break;
            }
            break;
        default:
            break;
        }

        if (aabb.minX == zer && aabb.maxX == one) {
            aabb.minX = min;
            aabb.maxX = max;
        }
        if (aabb.minY == zer && aabb.maxY == one) {
            aabb.minY = min;
            aabb.maxY = max;
        }
        if (aabb.minZ == zer && aabb.maxZ == one) {
            aabb.minZ = min;
            aabb.maxZ = max;
        }

        return aabb;
    }

    // private AxisAlignedBB getStripHitboxForSide(ForgeDirection dir) {
    //
    // ForgeDirection face = ForgeDirection.getOrientation(getFace());
    // AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    //
    // switch (face) {
    // case UP:
    // switch (dir) {
    // case EAST:
    // aabb = AxisAlignedBB.getBoundingBox(7 / 8D, 0, 1 / 8D, 1, 1 / 8D, 7 / 8D);
    // break;
    // case WEST:
    // aabb = AxisAlignedBB.getBoundingBox(0, 0, 1 / 8D, 1 / 8D, 1 / 8D, 7 / 8D);
    // break;
    // case NORTH:
    // aabb = AxisAlignedBB.getBoundingBox(1 / 8D, 0, 0, 7 / 8D, 1 / 8D, 1 / 8D);
    // break;
    // case SOUTH:
    // aabb = AxisAlignedBB.getBoundingBox(1 / 8D, 0, 7 / 8D, 7 / 8D, 1 / 8D, 1);
    // break;
    // default:
    // break;
    // }
    // break;
    // case DOWN:
    // switch (dir) {
    // case EAST:
    // break;
    // case WEST:
    // break;
    // case NORTH:
    // break;
    // case SOUTH:
    // break;
    // default:
    // break;
    // }
    // break;
    // case EAST:
    // switch (dir) {
    // case UP:
    // break;
    // case DOWN:
    // break;
    // case NORTH:
    // break;
    // case SOUTH:
    // break;
    // default:
    // break;
    // }
    // break;
    // case WEST:
    // switch (dir) {
    // case UP:
    // break;
    // case DOWN:
    // break;
    // case NORTH:
    // break;
    // case SOUTH:
    // break;
    // default:
    // break;
    // }
    // break;
    // case NORTH:
    // switch (dir) {
    // case UP:
    // break;
    // case DOWN:
    // break;
    // case EAST:
    // break;
    // case WEST:
    // break;
    // default:
    // break;
    // }
    // break;
    // case SOUTH:
    // switch (dir) {
    // case UP:
    // break;
    // case DOWN:
    // break;
    // case EAST:
    // break;
    // case WEST:
    // break;
    // default:
    // break;
    // }
    // break;
    // default:
    // break;
    // }
    //
    // return aabb;
    // }

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
