/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

import com.bluepowermod.util.ItemStackUtils;
import net.minecraft.world.item.component.CustomData;

public class ItemStackHelper {

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {

        return itemStack1.isEmpty() && itemStack2.isEmpty() || !(itemStack1.isEmpty() || itemStack2.isEmpty())
                && itemStack1.getItem() == itemStack2.getItem() && itemStack1.getDamageValue() == itemStack2.getDamageValue()
                && !(itemStack1.has(DataComponents.CUSTOM_DATA) && !itemStack2.has(DataComponents.CUSTOM_DATA))
                && (itemStack1.has(DataComponents.CUSTOM_DATA) || itemStack1.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).equals(itemStack2.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)));
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
            //TODO: Make sure this is right)
            return stack1.getTags().anyMatch(s1 -> stack2.getTags().anyMatch(s2 -> s2 == s1));
        } else if (mode == 1) {
            return ItemStackUtils.isItemFuzzyEqual(stack1, stack2);
        } else {
            return stack1.getTags().anyMatch(s1 -> stack2.getTags().anyMatch(s2 -> s2 == s1)) && ItemStack.isSameItemSameComponents(stack1, stack2);
        }
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1 == ItemStack.EMPTY || stack2 == ItemStack.EMPTY ||
                (stack1.getItem() == stack2.getItem() &&
                        (stack2.getDamageValue() == stack1.getDamageValue()) &&
                        ItemStack.isSameItemSameComponents(stack2, stack1)) &&
                        stack1.isStackable();
    }

}
