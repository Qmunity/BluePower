package com.bluepowermod.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.util.ItemStackUtils;

public class ItemStackHelper {

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {

        return itemStack1 == null && itemStack2 == null || !(itemStack1 == null || itemStack2 == null)
                && itemStack1.getItem() == itemStack2.getItem() && itemStack1.getItemDamage() == itemStack2.getItemDamage()
                && !(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null)
                && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound));
    }

    /**
     * Mode is a WidgetFuzzySetting mode.
     * 
     * @param stack1
     * @param stack2
     * @param mode
     * @return
     */
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, int mode) {
        if (stack1 == null && stack2 != null)
            return false;
        if (stack1 != null && stack2 == null)
            return false;
        if (stack1 == null && stack2 == null)
            return true;

        if (mode == 0) {
            return OreDictionary.itemMatches(stack1, stack2, false);
        } else if (mode == 1) {
            return ItemStackUtils.isItemFuzzyEqual(stack1, stack2);
        } else {
            return OreDictionary.itemMatches(stack1, stack2, false) && ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }
}
