package com.bluepowermod.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.network.annotation.DescSynced;
import uk.co.qmunity.lib.tile.IRotatable;
import uk.co.qmunity.lib.tile.TileBase;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;

public class TileBluePowerBase extends TileBase implements IRotatable {
    private IPowerBase handler;
    @DescSynced
    private boolean isPowered;

    public TileBluePowerBase() {
        if (this instanceof IPowered)
            handler = BPApi.getInstance().getPowerApi().createPowerHandler((IPowered) this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!getWorldObj().isRemote && handler != null) {

            handler.update();
            if (worldObj.getWorldTime() % 20 == 0) {
                isPowered = handler.getVoltage() >= handler.getMaxVoltage() * 0.8;
            }
        }
    }

    public boolean isPowered() {
        return isPowered;
    }

    public IPowerBase getPowerHandler(ForgeDirection side) {

        return handler;
    }

    public boolean canConnectPower(ForgeDirection side, IPowered dev, ConnectionType type) {

        return true;
    }

    public boolean isNormalFace(ForgeDirection side) {

        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        if (handler != null)
            handler.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        if (handler != null)
            handler.writeToNBT(tagCompound);
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (handler != null)
            handler.disconnect();
    }

    @Override
    public void onNeighborBlockChanged() {

        super.onNeighborBlockChanged();
        if (handler != null)
            handler.onNeighborUpdate();
    }

    @Override
    protected void onTileLoaded() {

        super.onTileLoaded();
        if (handler != null)
            handler.onNeighborUpdate();
    }
}
