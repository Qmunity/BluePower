package com.bluepowermod.api.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnectionCache;

/**
 * @author Koen Beckers (K4Unl)
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

    /*
     * Functions for power
     */
    public float getAmpHourStored();

    public float getMaxAmpHour();

    public float removeEnergy(float amp);

    public float addEnergy(float amp);

    /*
     * Connections
     */

    public IConnectionCache<IPowerBase> getConnectionCache();

    public boolean isConnected(ForgeDirection side);

    public IPowered getDevice();

    public void disconnect();
}
