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

package com.bluepowermod.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotExclude extends Slot {
    
    Item filter;
    
    public SlotExclude(IInventory par1iInventory, int par2, int par3, int par4, Item filter) {
        super(par1iInventory, par2, par3, par4);
        
        this.filter = filter;
    }
    
    @Override
    public boolean mayPlace(ItemStack par1ItemStack) {
        return par1ItemStack.isEmpty() || !(par1ItemStack.getItem() == filter);
    }
}
