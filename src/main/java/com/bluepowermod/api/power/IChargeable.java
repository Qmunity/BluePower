package com.bluepowermod.api.power;

import net.minecraft.item.ItemStack;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IChargeable {

    public PowerTier getTier();

    public float getAmpStored(ItemStack stack);

    public float getMaxAmp();

    /**
     * ! Adds energy to the item.
     *
     * @param amp
     *            The amount of energy to add
     * @return The amount of energy that has been added
     */
    public float injectEnergy(ItemStack stack, float amp, boolean simulated);

    /**
     * ! Removes energy from the item.
     *
     * @param amp
     *            The amount of energy to drain
     * @return The amount of energy that has been drained
     */
    public float drainEnergy(ItemStack stack, float amp, boolean simulated);

}
