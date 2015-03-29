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

import java.util.Random;

import net.minecraft.item.ItemStack;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

public class ItemLimitedCrafting extends ItemBase {
    
    public ItemLimitedCrafting(String name, int uses) {
    
        this.setCreativeTab(BPCreativeTabs.items);
        this.setUnlocalizedName(name);
        this.setTextureName(Refs.MODID + ":" + name);
        this.setMaxDamage(uses - 1);
        
        this.setContainerItem(this);
    }
    
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
    
        return false;
    }
    
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
    
        ItemStack container = itemStack.copy();
        container.attemptDamageItem(1, new Random());
        return container;
    }
}
