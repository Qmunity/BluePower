package com.bluepowermod.api.bluepower;

import com.bluepowermod.api.vec.Vector3;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IPowerBase {

    /*
     * Forward these functions to the handler
     */

    public void readFromNBT(NBTTagCompound tagCompound);

    public void writeToNBT(NBTTagCompound tagCompound);

    /*
     * Functions for power
     */
    public float getAmpStored();

    public float getMaxAmp();

    public void removeEnergy(float amp);

    public void addEnergy(float amp);

    public Vector3 getBlockLocation();

    void update();

    void invalidate();
}
