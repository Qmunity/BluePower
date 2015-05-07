package com.bluepowermod.api.bluepower;

import net.minecraft.item.ItemStack;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IRechargeable {

    public BluePowerTier getTier();

    public float getAmpStored(ItemStack stack);

    public float getMaxAmp();

    /**!
     * Removes energy from the rechargeable item.
     * @param amp The amount of energy to drain
     * @return The amount of energy that has been drained
     */
    public float removeEnergy(ItemStack stack, float amp);

    /**!
     * Adds energy to the rechargeable item.
     * @param amp The amount of energy to add
     * @return The amount of energy that has been added
     */
    public float addEnergy(ItemStack stack, float amp);

}
