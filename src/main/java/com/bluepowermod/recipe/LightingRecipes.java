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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.reference.Refs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Quetzi on 27/10/14.
 */
public class LightingRecipes {

    public static void init() {

        for (int i = 0; i < Refs.oreDictDyes.length; i++) {
            // Lumar
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.lumar, 2, 15 - i), new ItemStack(Items.REDSTONE, 1), new ItemStack(
                    Items.GLOWSTONE_DUST, 1), Refs.oreDictDyes[i], Refs.oreDictDyes[i]));

            // Full block lamps
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLamp[MinecraftColor.VALID_COLORS.length - 1 - i], 1), "gLg",
                    "gLg", "gRg", 'g', "paneGlassColorless", 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R', "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLampInverted[MinecraftColor.VALID_COLORS.length - 1 - i], 1),
                    "gLg", "gLg", "gRg", 'g', "paneGlassColorless", 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R', Blocks.REDSTONE_TORCH));

            // Microblock lamps
            String colorname = MinecraftColor.values()[MinecraftColor.VALID_COLORS.length - 1 - i].name().toLowerCase();
            GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("fixture." + colorname).getStack(), "gLg", "gLg", "sRs", 'g',
                    "paneGlassColorless", 's', new ItemStack(Blocks.STONE_SLAB, 1, 0), 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R',
                    "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("fixture." + colorname + ".inverted").getStack(), "gLg", "gLg", "sRs",
                    'g', "paneGlassColorless", 's', new ItemStack(Blocks.STONE_SLAB, 1, 0), 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R',
                    Blocks.REDSTONE_TORCH));
            GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("cagelamp." + colorname).getStack(), "cLc", "gLg", "sRs", 'g',
                    "paneGlassColorless", 'c', Blocks.IRON_BARS, 's', new ItemStack(Blocks.STONE_SLAB, 1, 0), 'L', new ItemStack(BPItems.lumar, 1,
                            15 - i), 'R', "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("cagelamp." + colorname + ".inverted").getStack(), "cLc", "gLg",
                    "sRs", 'g', "paneGlassColorless", 'c', Blocks.IRON_BARS, 's', new ItemStack(Blocks.STONE_SLAB, 1, 0), 'L', new ItemStack(
                            BPItems.lumar, 1, 15 - i), 'R', Blocks.REDSTONE_TORCH));

        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPBlocks.blockLampRGB, 4), "lampBP", "lampBP", "lampBP", "lampBP"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPBlocks.blockLampRGBInverted, 4), "lampInvertedBP", "lampInvertedBP",
                "lampInvertedBP", "lampInvertedBP"));
    }
}
