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

import com.bluepowermod.recipe.CoreRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;

public class Recipes {

    public static void init() {

        CoreRecipes.init();
        //LogicRecipes.init();
        //MachineRecipes.init();
        //LightingRecipes.init();

        // Alloy furnace
        IAlloyFurnaceRegistry af = BPApi.getInstance().getAlloyFurnaceRegistry();

        af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.REDSTONE, 4), Items.IRON_INGOT);
        af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.REDSTONE, 4), BPItems.copper_ingot);

        af.addRecipe(new ItemStack(BPItems.blue_alloy_ingot, 1), new ItemStack(BPItems.teslatite_dust, 4), BPItems.silver_ingot);

        af.addRecipe(new ItemStack(BPItems.purple_alloy_ingot, 1), BPItems.blue_alloy_ingot, BPItems.red_alloy_ingot);
        af.addRecipe(new ItemStack(BPItems.purple_alloy_ingot, 1), Items.GOLD_INGOT, new ItemStack(BPItems.infused_teslatite_dust, 8));

        af.addRecipe(new ItemStack(BPItems.brass_ingot, 4), new ItemStack(BPItems.copper_ingot, 3), BPItems.zinc_ingot);
        af.addRecipe(new ItemStack(BPItems.silicon_boule, 1), new ItemStack(Items.COAL, 8), new ItemStack(Blocks.SAND, 8));

        af.addRecipe(new ItemStack(BPItems.red_doped_wafer, 1), new ItemStack(Items.REDSTONE, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.blue_doped_wafer, 1), new ItemStack(BPItems.teslatite_dust, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.zincplate, 4), new ItemStack(BPItems.zinc_ingot, 1), new ItemStack(Items.IRON_INGOT, 2));

        af.addRecyclingRecipe(new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.IRON_INGOT, 9));
        af.addRecyclingRecipe(new ItemStack(Blocks.GOLD_BLOCK), new ItemStack(Items.GOLD_INGOT, 9));
        af.addRecyclingRecipe(new ItemStack(Items.IRON_INGOT));
        af.addRecyclingRecipe(new ItemStack(Items.GOLD_INGOT));
        af.addRecyclingRecipe(new ItemStack(Items.GOLD_NUGGET));

        af.addRecipe(new ItemStack(BPBlocks.sapphire_glass, 4), new ItemStack(Blocks.GLASS, 4), BPItems.sapphire_gem);
        af.addRecipe(new ItemStack(BPBlocks.reinforced_sapphire_glass, 1), new ItemStack(BPBlocks.sapphire_glass, 1), new ItemStack(
                Blocks.OBSIDIAN, 5));
    }
}