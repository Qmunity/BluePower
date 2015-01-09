package com.bluepowermod.api.bluepower;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.vec.Vec3i;

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

    public Vec3i getBlockLocation();

    void update();

    void invalidate();
}
