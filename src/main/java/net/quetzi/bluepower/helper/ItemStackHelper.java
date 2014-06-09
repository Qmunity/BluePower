package net.quetzi.bluepower.helper;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {

        return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.getItem() == itemStack2.getItem() && (
                itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (
                        itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
    }
}
