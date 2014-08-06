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

package com.bluepowermod.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * This class only should be used for special recipes, like recipes that depend on NBT. For normal recipes use
 * {@link com.bluepowermod.recipe.AlloyFurnaceRegistry#addRecipe(ItemStack, ItemStack...)}.
 * @author MineMaarten
 */
public interface IAlloyFurnaceRecipe {
    
    /**
     * Return true if this recipe can be smelted using the input stacks. The input stacks are the 9 inventory slots, so an element can be null.
     *
     * @param input
     * @return
     */
    boolean matches(ItemStack[] input);
    
    /**
     * The items that are needed in this recipe need to be removed from the input inventory.
     */
    void useItems(ItemStack[] input);
    
    ItemStack getCraftingResult(ItemStack[] input);
}
