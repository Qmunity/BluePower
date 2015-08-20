package com.bluepowermod.api.power;

import net.minecraft.item.ItemStack;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IRechargeable {

    public int getVoltage(ItemStack stack);

    public int getMaxVoltage();

    /**
     * Adds energy to the rechargeable item, or removes energy (if the energy is negative)
     * @param stack The itemstack to apply to
     * @param energy The amount of energy to add
     * @return The amount of energy that has been added
     */
    public int addEnergy(ItemStack stack, int energy);

}
