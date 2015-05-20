package com.bluepowermod.api.power;

import net.minecraft.item.ItemStack;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IRechargeable {

    public PowerTier getTier();

    public float getAmpStored(ItemStack stack);

    public float getMaxAmp();

    /**!
     * Removes energy from the rechargeable item.
     * @param ampHour The amount of energy to drain
     * @return The amount of energy that has been drained
     */
    public float removeEnergy(ItemStack stack, float ampHour);

    /**!
     * Adds energy to the rechargeable item.
     * @param ampHour The amount of energy to add
     * @return The amount of energy that has been added
     */
    public float addEnergy(ItemStack stack, float ampHour);

}
