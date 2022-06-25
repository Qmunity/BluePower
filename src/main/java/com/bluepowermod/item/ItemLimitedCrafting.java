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
 *     
 *     @author Lumien
 */

package com.bluepowermod.item;

import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class ItemLimitedCrafting extends ItemBase {
    
    public ItemLimitedCrafting(int uses) {
        super(new Properties().durability(uses - 1));
    }
    
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        return container.hurt(1, new Random(), null) ? ItemStack.EMPTY : container;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
