package com.bluepowermod.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IMachineBlueprint {

    public void saveMachineSettings(ItemStack blueprint, String type, NBTTagCompound settings);

    public void clearSettings(ItemStack blueprint);

    public String getStoredSettingsType(ItemStack blueprint);

    public NBTTagCompound getStoredSettings(ItemStack blueprint);

}
