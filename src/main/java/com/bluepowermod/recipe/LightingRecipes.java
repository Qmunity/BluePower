/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.recipe;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Quetzi on 27/10/14.
 */
public class LightingRecipes {

    public static void init() {

        for (int i = 0; i < Refs.oreDictDyes.length; i++) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.lumar, 2, 15 - i), new ItemStack(Items.redstone, 1), new ItemStack(
                    Items.glowstone_dust, 1), Refs.oreDictDyes[i], Refs.oreDictDyes[i]));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLamp[i], 1), "gLg", "gLg", "gRg", 'g', "paneGlassColorless", 'L',
                    new ItemStack(BPItems.lumar, 1, 15 - i), 'R', "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLampInverted[i], 1), "gLg", "gLg", "gRg", 'g',
                    "paneGlassColorless", 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R', Blocks.redstone_torch));
            // other multipart lamps
            //TODO: Add Part recipes
            /*
            GameRegistry.addRecipe(new ShapedOreRecipe(
                    PartRegistry.getInstance().getItemForPart("fixture" + ItemDye.field_150921_b[i].toLowerCase()), "gLg", "gLg", "sRs", 'g',
                    "paneGlassColorless", 's', new ItemStack(Blocks.stone_slab, 1, 0), 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R',
                    "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart(
                    "invertedfixture" + ItemDye.field_150921_b[i].toLowerCase()), "gLg", "gLg", "sRs", 'g', "paneGlassColorless", 's', new ItemStack(
                    Blocks.stone_slab, 1, 0), 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R', Blocks.redstone_torch));
            GameRegistry
                    .addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("cagelamp" + ItemDye.field_150921_b[i].toLowerCase()),
                            "cLc", "gLg", "sRs", 'g', "paneGlassColorless", 'c', Blocks.iron_bars, 's', new ItemStack(Blocks.stone_slab, 1, 0), 'L',
                            new ItemStack(BPItems.lumar, 1, 15 - i), 'R', "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart(
                    "invertedcagelamp" + ItemDye.field_150921_b[i].toLowerCase()), "cLc", "gLg", "sRs", 'g', "paneGlassColorless", 'c',
                    Blocks.iron_bars, 's', new ItemStack(Blocks.stone_slab, 1, 0), 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R',
                    Blocks.redstone_torch));
                    */
        }
    }
}
