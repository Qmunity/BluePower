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
 * 
 * @author MineMaarten
 */

public interface IAlloyFurnaceRegistry {

    /**
     * With this you can add recipes that require special handling (like NBT dependent recipes). It's similar to Vanilla's IRecipe
     * For the normal recipes, use addRecipe(ItemStack output, Object... input).
     *
     * @param recipe
     */
    void addRecipe(IAlloyFurnaceRecipe recipe);

    /**
     * Adds a recipe to the Alloy Furnace.
     *
     * @param output the crafting result
     * @param input  input items. These can be ItemStack, Item or Block objects. You can specify up to 9 input objects.
     *               You can only specify one type of item once. Items will automatically be matched against the ore dictionary.
     */
    void addRecipe(ItemStack output, Object... input);

    /**
     * Any item added here will cause dynamically generated recipes that allows items to be broken down to this item.
     * In BluePower, this is called with an iron ingot.S
     *
     * @param recycledItem
     * @param blacklist list of item registry names ("minecraft:bucket") that shouldn't be added by the generator.
     * As modder you could pass a config entry to this.
     */
    void addRecyclingRecipe(ItemStack recycledItem, String... blacklist);

    void addRecyclingRecipe(ItemStack recycledItem, ItemStack moltenDownItem, String... blacklist);
}
