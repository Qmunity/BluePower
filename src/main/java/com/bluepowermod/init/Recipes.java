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

package com.bluepowermod.init;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;

public class Recipes {

    public static void init() {

        // Alloy furnace
        IAlloyFurnaceRegistry af = BPApi.getInstance().getAlloyFurnaceRegistry();

        af.addRecyclingRecipe(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.IRON_INGOT, 9));
        af.addRecyclingRecipe(new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Items.GOLD_INGOT, 9));
        af.addRecyclingRecipe(new ItemStack(Items.IRON_INGOT));
        af.addRecyclingRecipe(new ItemStack(Items.GOLD_INGOT));
        af.addRecyclingRecipe(new ItemStack(Items.GOLD_NUGGET));

    }
}