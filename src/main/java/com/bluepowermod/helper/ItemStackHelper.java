/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.util.ItemStackUtils;

public class ItemStackHelper {

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {

        return itemStack1.isEmpty() && itemStack2.isEmpty() || !(itemStack1.isEmpty() || itemStack2.isEmpty())
                && itemStack1.getItem() == itemStack2.getItem() && itemStack1.getItemDamage() == itemStack2.getItemDamage()
                && !(itemStack1.getTagCompound() == null && itemStack2.getTagCompound() != null)
                && (itemStack1.getTagCompound() == null || itemStack1.getTagCompound().equals(itemStack2.getTagCompound()));
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
        if (stack1.isEmpty() && !stack2.isEmpty())
            return false;
        if (!stack1.isEmpty() && stack2.isEmpty())
            return false;
        if (stack1.isEmpty() && stack2.isEmpty())
            return true;

        if (mode == 0) {
            return OreDictionary.itemMatches(stack1, stack2, false);
        } else if (mode == 1) {
            return ItemStackUtils.isItemFuzzyEqual(stack1, stack2);
        } else {
            return OreDictionary.itemMatches(stack1, stack2, false) && ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1 == ItemStack.EMPTY || stack2 == ItemStack.EMPTY ||
                (stack1.getItem() == stack2.getItem() &&
                        (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) &&
                        ItemStack.areItemStackTagsEqual(stack2, stack1)) &&
                        stack1.isStackable();
    }

}
