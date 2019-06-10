/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.util;

import com.bluepowermod.compat.CompatibilityUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author MineMaarten, amadornes
 */

public class ItemStackUtils {

    public static boolean isItemFuzzyEqual(ItemStack stack1, ItemStack stack2) {

        if (isSameOreDictStack(stack1, stack2)) {
            return true;
        }
        if (stack1.getItem() != stack2.getItem())
            return false;
        return stack1.equals(stack2);
    }

    public static boolean isSameOreDictStack(ItemStack stack1, ItemStack stack2) {
        for (ResourceLocation resTag : ItemTags.getCollection().getOwningTags(stack1.getItem())) {
            if (ItemTags.getCollection().get(resTag) != null) {
                return ItemTags.getCollection().get(resTag).contains(stack2.getItem());
            }
        }
        return false;
    }

    public static boolean isScrewdriver(ItemStack item) {

        if (item.isEmpty())
            return false;
        if (item.getItem() == null)
            return false;

        return CompatibilityUtils.isScrewdriver(item);
    }
}
