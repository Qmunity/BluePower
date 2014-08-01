package com.bluepowermod.part.cable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.compat.fmp.IMultipartCompat;
import com.bluepowermod.references.Dependencies;
import com.bluepowermod.util.ForgeDirectionUtils;

/**
 * @author amadornes
 * 
 */
public abstract class CableWall extends BPPartFace {

    protected Object[] connections = new Object[6];
    private Vector3 loc;

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

    @Override
    public void onPartChanged() {

        onUpdate();
    }

    @Override
    public void onNeighborUpdate() {

        onUpdate();
    }

    @Override
    public void onNeighborTileUpdate() {

        onUpdate();
    }

    @Override
    public void onFirstTick() {

        onUpdate();
    }

    public void onUpdate() {

        if (loc == null || loc.getBlockX() != x || loc.getBlockY() != y || loc.getBlockZ() != z || loc.getWorld() != world)
            loc = new Vector3(x, y, z, world);

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (!canConnectOnSide(d))
                continue;

            Vector3 vec = loc.getRelative(d);

            // Check connection to cables
            CableWall c = getCableOnSide(d);
            if (canConnectToCable(c)) {
                connections[ForgeDirectionUtils.getSide(d)] = c;
                continue;
            }

            // Check connection to blocks
            if (vec.getBlock(true) != null && canConnectToBlock(vec.getBlock(), vec)) {
                connections[ForgeDirectionUtils.getSide(d)] = vec;
                continue;
            }

            // Check connection to TEs
            if (vec.getTileEntity() != null && canConnectToTileEntity(vec.getTileEntity())) {
                connections[ForgeDirectionUtils.getSide(d)] = vec;
                continue;
            }
            connections[ForgeDirectionUtils.getSide(d)] = null;
        }
    }

    private CableWall getCableOnSide(ForgeDirection dir) {

        Vector3 vec = loc.getRelative(dir);

        List<CableWall> l = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getBPParts(vec.getTileEntity(), CableWall.class);
        for (CableWall c : l)
            if (c.getFace() == getFace())
                return c;
        l.clear();

        l = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getBPParts(loc.getTileEntity(), CableWall.class);
        for (CableWall c : l)
            if (ForgeDirection.getOrientation(c.getFace()) == dir)
                return c;

        return null;
    }
}
