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
import com.bluepowermod.part.PartManager;
import com.bluepowermod.reference.Refs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Created by Quetzi on 27/10/14.
 */
public class MachineRecipes {

    public static void init() {

        // Components
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.copper_wire, 1), new ItemStack(BPItems.diamond_drawplate, 1,
                OreDictionary.WILDCARD_VALUE), "ingotCopper"));

        // Tubes and Transport
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("pneumaticTube").getStack(8), "BGB", 'B', "ingotBrass", 'G',
                "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("pneumaticTubeOpaque").getStack(8), "BGB", 'B', "ingotBrass",
                'G', "stone"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(PartManager.getPartInfo("restrictionTube").getStack(), "ingotIron", PartManager
                .getPartInfo("pneumaticTube").getStack()));
        GameRegistry.addRecipe(new ShapelessOreRecipe(PartManager.getPartInfo("restrictionTubeOpaque").getStack(), "ingotIron", PartManager
                .getPartInfo("pneumaticTubeOpaque").getStack()));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("magTube").getStack(8), "CCC", "BGB", "CCC", 'B',
                Blocks.OBSIDIAN, 'G', "blockGlass", 'C', BPItems.copper_wire));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("accelerator").getStack(), "OIO", "I I", "OIO", 'O',
                Blocks.OBSIDIAN, 'I', "ingotBlueAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("accelerator").getStack(), "IOI", "O O", "IOI", 'O',
                Blocks.OBSIDIAN, 'I', "ingotBlueAlloy"));

        // Paint
        GameRegistry.addRecipe(new ItemStack(BPItems.paint_can, 1, 16), "t t", "t t", "ttt", 't', BPItems.zincplate);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.paint_brush, 1, 16), " w", "s ", 'w', Blocks.WOOL, 's',
                "stickWood"));
        for (int i = 0; i < Refs.oreDictDyes.length; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.paint_brush, 1, i), new ItemStack(BPItems.paint_can, 1, i),
                    new ItemStack(BPItems.paint_brush, 1, OreDictionary.WILDCARD_VALUE));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.paint_can, 1, i), new ItemStack(BPItems.flax_seeds),
                    new ItemStack(BPItems.flax_seeds), Refs.oreDictDyes[i], new ItemStack(BPItems.paint_can, 1, 16)));
        }

        // Blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.block_breaker, 1), "#A#", "#P#", "#R#", '#', "cobblestone", 'A',
                Items.IRON_PICKAXE, 'P', Blocks.PISTON, 'R', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.buffer, 1), "#P#", "P P", "#P#", '#', Blocks.IRON_BARS, 'P',
                "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.ejector, 1), "PBP", "PTP", "#R#", '#', "cobblestone", 'P',
                "plankWood", 'B', BPBlocks.buffer, 'T', BPBlocks.transposer, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.relay, 1), "PBP", "PTP", "#W#", '#', "cobblestone", 'P',
                "plankWood", 'B', BPBlocks.buffer, 'T', BPBlocks.transposer, 'W', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.transposer, 1), "###", "WPW", "#R#", '#', "cobblestone", 'P',
                Blocks.PISTON, 'R', "dustRedstone", 'W', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.item_detector, 1), "ITI", "WUW", "PTP", 'I', "ingotBrass", 'T',
                PartManager.getPartInfo("pneumaticTube").getStack(), 'W', BPItems.red_doped_wafer, 'U', Blocks.WOODEN_PRESSURE_PLATE, 'P',
                "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.regulator, 1), "IBI", "WDW", "PBP", 'I', "ingotBrass", 'B',
                BPBlocks.buffer, 'W', BPItems.red_doped_wafer, 'D', BPBlocks.item_detector, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.retriever, 1), "BLB", "EFE", "IAI", 'B', "ingotBrass", 'L',
                Items.LEATHER, 'E', Items.ENDER_PEARL, 'F', BPBlocks.filter, 'I', "ingotIron", 'A', "ingotBlueAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.filter, 1), "CCC", "GPG", "CWC", 'C', "cobblestone", 'G',
                "ingotGold", 'P', Blocks.PISTON, 'W', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.sorting_machine, 1), "CCC", "GPG", "CWC", 'C', "ingotIron", 'G',
                BPItems.red_doped_wafer, 'P', BPBlocks.filter, 'W', "ingotBlueAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.igniter, 1), "NFN", "CDC", "CRC", 'C', "cobblestone", 'N',
                Blocks.NETHERRACK, 'F', Items.FLINT_AND_STEEL, 'R', "dustRedstone", 'D', BPBlocks.deployer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.deployer, 1), "CIC", "CPC", "CRC", 'C', "cobblestone", 'I',
                Blocks.CHEST, 'P', Blocks.PISTON, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.manager, 1), "IRI", "WSW", "PBP", 'I', "ingotIron", 'R',
                BPBlocks.regulator, 'P', "plankWood", 'W', BPItems.red_doped_wafer, 'B', "ingotBlueAlloy", 'S', BPBlocks.sorting_machine));
    }
}
