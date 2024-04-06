/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.world.item.ItemStack;

public class ItemPaintCan extends ItemDamageableColorableOverlay {

    private static final int MAX_USES = 16;

    public ItemPaintCan() {
        super(new Properties());
    }

    public ItemPaintCan(MinecraftColor color) {
        super(color, new Properties());
    }

    @Override
    protected int getMaxUses() {
        return MAX_USES;
    }


    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (itemStack.getDamageValue() >= 16)
            return ItemStack.EMPTY;
        ItemStack stack = itemStack.copy();
        int newUses = getUsesUsed(stack) + 1;

        if (newUses == getMaxUses()) {
            stack.setDamageValue(16);
            setUsesUsed(stack, 0);
        } else {
            setUsesUsed(stack, newUses);
        }
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return !getCraftingRemainingItem(stack).isEmpty();
    }
}
