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

import net.minecraft.core.HolderLookup;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

/**
 *  @author MoreThanHidden
 */
public interface IAlloyFurnaceRecipe extends Recipe<WorldlyContainer>  {
    
    /**
     * Return true if this recipe can be smelted using the input stacks. The input stacks are the 9 inventory slots, so an element can be ItemStack.EMPTY.
     *
     * @param input
     * @return
     */
    boolean matches(NonNullList<ItemStack> input);

    /**
     * The items that are needed in this recipe need to be removed from the input inventory.
     */
    boolean useItems(NonNullList<ItemStack> input, HolderLookup.Provider provider);

    ItemStack assemble(NonNullList<ItemStack> input, HolderLookup.Provider provider);

    NonNullList<SizedIngredient> getRequiredItems();

    ItemStack getCraftingResult();

}
