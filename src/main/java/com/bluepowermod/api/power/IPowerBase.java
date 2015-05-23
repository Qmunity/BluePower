package com.bluepowermod.api.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnectionCache;

/**
 * @author MineMaarten, Koen Beckers (K4Unl)
 */
public interface IPowerBase {

    /*
     * Forward these functions to the handler
     */

    public void readFromNBT(NBTTagCompound tagCompound);

    public void writeToNBT(NBTTagCompound tagCompound);

    public void readUpdateFromNBT(NBTTagCompound tagCompound);

    public void writeUpdateToNBT(NBTTagCompound tagCompound);

    public void update();

    public void onNeighborUpdate();

    /**
     * Negative energy for removal
     * @param energy
     * @param when true, no power will be added, but the return value can be used to determine if adding power is possible.
     * @return the added power.
     */
    public double addEnergy(double energy, boolean simulate);

    public double getVoltage();

    public double getMaxVoltage();

    /*
     * Connections
     */

    public IConnectionCache<IPowerBase> getConnectionCache();

    public boolean isConnected(ForgeDirection side);

    public IPowered getDevice();

    public void disconnect();
}
