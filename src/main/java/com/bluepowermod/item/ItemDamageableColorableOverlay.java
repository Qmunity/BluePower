/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

/**
 * @author MineMaarten
 */
public abstract class ItemDamageableColorableOverlay extends ItemColorableOverlay {

    public ItemDamageableColorableOverlay(String name, Properties properties) {
        super(name, properties);
    }


    public static int getUsesUsed(ItemStack stack) {

        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            return tag.getInt("usesUsed");
        } else {
            return 0;
        }
    }

    public static void setUsesUsed(ItemStack stack, int newUses) {

        CompoundNBT tag = stack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
            stack.setTag(tag);
        }
        tag.putInt("usesUsed", newUses);
    }

    public boolean tryUseItem(ItemStack stack) {

        if (stack.getDamage() == 16)
            return true;
        if (getUsesUsed(stack) < getMaxUses()) {
            int newUses = getUsesUsed(stack) + 1;
            if (newUses < getMaxUses()) {
                setUsesUsed(stack, newUses);
            } else {
                setUsesUsed(stack, 0);
                stack.setDamage(16);
            }
            return true;
        } else {
            return false;
        }
    }

    protected abstract int getMaxUses();

    /**
     * Determines if the durability bar should be rendered for this item. Defaults to vanilla stack.isDamaged behavior. But modders can use this for
     * any data they wish.
     *
     * @param stack
     *            The current Item Stack
     * @return True if it should render the 'durability' bar.
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {

        return getUsesUsed(stack) != 0;
    }

    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack
     *            The current ItemStack
     * @return 1.0 for 100% 0 for 0%
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        return (double) getUsesUsed(stack) / (double) getMaxUses();
    }
}
