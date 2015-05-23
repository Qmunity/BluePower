package com.bluepowermod.api.power;

import net.minecraft.item.ItemStack;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IRechargeable {

    public int getAmpStored(ItemStack stack);

    public float getMaxAmp();

    /**
     * Adds energy to the rechargeable item, or removes energy (if the energy is negative)
     * @param ampHour The amount of energy to add
     * @return The amount of energy that has been added
     */
    public int addEnergy(ItemStack stack, int energy);

}
