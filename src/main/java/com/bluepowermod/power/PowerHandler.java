package com.bluepowermod.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;

/**
 * @author Koen Beckers (K4Unl)
 */
public class PowerHandler implements IPowerBase, IFace {

    private IPowered device;
    private PowerConnectionCache cache = new PowerConnectionCache(this);
    private boolean[] connections = new boolean[6];

    public PowerHandler(IPowered device) {

        this.device = device;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        // TODO
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        // TODO
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tagCompound) {

        NBTTagCompound tag = tagCompound.getCompoundTag("powerHandler");
        for (int i = 0; i < 6; i++)
            connections[i] = tag.getBoolean("connected_" + i);

        // TODO
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tagCompound) {

        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < 6; i++)
            tag.setBoolean("connected_" + i, cache.getConnectionOnSide(ForgeDirection.getOrientation(i)) != null);
        tagCompound.setTag("powerHandler", tag);

        // TODO
    }

    @Override
    public void update() {

        // TODO
    }

    @Override
    public void onNeighborUpdate() {

        cache.recalculateConnections();
    }

    @Override
    public float getAmpHourStored() {

        return 0; // TODO
    }

    @Override
    public float getMaxAmpHour() {

        return 0; // TODO
    }

    @Override
    public float removeEnergy(float ampHour) {

        return 0; // TODO
    }

    @Override
    public float addEnergy(float ampHour) {

        return 0; // TODO
    }

    @Override
    public IConnectionCache<IPowerBase> getConnectionCache() {

        return cache;
    }

    @Override
    public boolean isConnected(ForgeDirection side) {

        if (device.getWorld() != null && !device.getWorld().isRemote)
            return cache.getConnectionOnSide(side) != null;

        return connections[side.ordinal()];
    }

    @Override
    public IPowered getDevice() {

        return device;
    }

    @Override
    public void disconnect() {

        cache.disconnectAll();
    }

    @Override
    public ForgeDirection getFace() {

        if (device instanceof IFace)
            return ((IFace) device).getFace();

        return ForgeDirection.UNKNOWN;
    }
}
