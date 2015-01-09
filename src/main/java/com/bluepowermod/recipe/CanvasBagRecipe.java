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

package com.bluepowermod.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.bluepowermod.item.ItemCanvasBag;

public class CanvasBagRecipe extends ShapelessOreRecipe {
    
    public CanvasBagRecipe(ItemStack result, Object... recipe) {
    
        super(result, recipe);
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
    
        ItemStack bag=null;
        for (int slot=0;slot<inventoryCrafting.getSizeInventory();slot++)
        {
            if (inventoryCrafting.getStackInSlot(slot)!=null && inventoryCrafting.getStackInSlot(slot).getItem() instanceof ItemCanvasBag)
            {
                bag = inventoryCrafting.getStackInSlot(slot);
            }
        }
        if (bag!=null)
        {
            bag=bag.copy();
            bag.setItemDamage(this.getRecipeOutput().getItemDamage());
            return bag;
        }
        
        return this.getRecipeOutput();
    }
}
