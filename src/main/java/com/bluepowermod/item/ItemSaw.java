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

package com.bluepowermod.item;

import java.util.Random;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPCreativeTabs;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public class ItemSaw extends ItemBase{

    private final int sawLevel;

    public ItemSaw(int sawLevel) {
        super(new Properties().stacksTo(1).durability(1 << sawLevel + 8));
        this.sawLevel = sawLevel;
    }

    public int getSawLevel() {
        return sawLevel;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        container.hurt(1, RandomSource.create(), null);
        return container;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}