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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.recipe.CanvasBagRecipe;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init(CraftingManager craftManager) {

        GameRegistry.addSmelting(BPBlocks.basalt_cobble, new ItemStack(BPBlocks.basalt), 0);
        GameRegistry.addSmelting(BPBlocks.copper_ore, new ItemStack(BPItems.copper_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.zinc_ore, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.silver_ore, new ItemStack(BPItems.silver_ingot), 0.7F);
        GameRegistry.addSmelting(BPBlocks.tungsten_ore, new ItemStack(BPItems.tungsten_ingot), 0.8F);
        GameRegistry.addSmelting(BPBlocks.basalt, new ItemStack(BPBlocks.basalt_tile), 0);
        GameRegistry.addSmelting(BPBlocks.marble, new ItemStack(BPBlocks.marble_tile), 0);
        GameRegistry.addSmelting(Blocks.stone, new ItemStack(BPItems.stone_tile, 2), 0);
        GameRegistry.addSmelting(BPBlocks.basalt_brick, new ItemStack(BPBlocks.basaltbrick_cracked, 1), 0);
        GameRegistry.addSmelting(BPItems.zinc_ore_crushed, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_dust, new ItemStack(BPItems.zinc_ingot), 0.5F);
        GameRegistry.addSmelting(BPItems.zinc_ore_crushed_purified, new ItemStack(BPItems.zinc_ingot), 0.5F);

        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.basalt_paver), BPBlocks.basalt_tile);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.basalt_tile), BPBlocks.basalt_paver);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.marble_paver), BPBlocks.marble_tile);
        GameRegistry.addShapelessRecipe(new ItemStack(BPBlocks.marble_tile), BPBlocks.marble_paver);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.basalt_brick, 4), "##", "##", '#', BPBlocks.basalt));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.marble_brick, 4), "##", "##", '#', BPBlocks.marble));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.copper_block, 1), "###", "###", "###", '#', "ingotCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.silver_block, 1), "###", "###", "###", '#', "ingotSilver"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.zinc_block, 1), "###", "###", "###", '#', "ingotZinc"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.tungsten_block, 1), "###", "###", "###", '#', "ingotTungsten"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.amethyst_block, 1), "###", "###", "###", '#', "gemAmethyst"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.ruby_block, 1), "###", "###", "###", '#', "gemRuby"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.sapphire_block, 1), "###", "###", "###", '#', "gemSapphire"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.teslatite_block, 1), "###", "###", "###", '#', "dustTeslatite"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_axe, 1), "GG ", "GS ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_axe, 1), " GG", " SG", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_sword, 1), "G", "G", "S", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_spade, 1), "G", "S", "S", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_hoe, 1), "GG ", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_hoe, 1), " GG", " S ", " S ", 'G', "gemRuby", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_sickle, 1), " G ", "  G", "SG ", 'G', "gemRuby", 'S', "stickWood"));

        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_axe, 1), "GG ", "GS ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_axe, 1), " GG", " SG", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemSapphire", 'S',
                "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_sword, 1), "G", "G", "S", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_spade, 1), "G", "S", "S", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_hoe, 1), "GG ", " S ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_hoe, 1), " GG", " S ", " S ", 'G', "gemSapphire", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_sickle, 1), " G ", "  G", "SG ", 'G', "gemSapphire", 'S',
                "stickWood"));

        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_axe, 1), "GG ", "GS ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_axe, 1), " GG", " SG", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_pickaxe, 1), "GGG", " S ", " S ", 'G', "gemAmethyst", 'S',
                "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_sword, 1), "G", "G", "S", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_spade, 1), "G", "S", "S", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_hoe, 1), "GG ", " S ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_hoe, 1), " GG", " S ", " S ", 'G', "gemAmethyst", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_sickle, 1), " G ", "  G", "SG ", 'G', "gemAmethyst", 'S',
                "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.wood_sickle, 1), " G ", "  G", "SG ", 'G', "plankWood", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_sickle, 1), " G ", "  G", "SG ", 'G', "cobblestone", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.iron_sickle, 1), " G ", "  G", "SG ", 'G', "ingotIron", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.gold_sickle, 1), " G ", "  G", "SG ", 'G', "ingotGold", 'S', "stickWood"));
        GameRegistry
                .addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.diamond_sickle, 1), " G ", "  G", "SG ", 'G', "gemDiamond", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.iron_saw, 1), "SSS", " II", " II", 'S', "stickWood", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.ruby_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron", '#',
                "gemRuby"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.amethyst_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron",
                '#', "gemAmethyst"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.sapphire_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron",
                '#', "gemSapphire"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.diamond_saw, 1), "SSS", " II", " ##", 'S', "stickWood", 'I', "ingotIron",
                '#', "gemDiamond"));

        craftManager.addRecipe(new ItemStack(BPItems.indigo_dye, 1), "#", '#', BPBlocks.indigo_flower);

        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick_small, 4), "##", "##", '#', BPBlocks.basalt_brick);
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick_small, 4), "##", "##", '#', BPBlocks.marble_brick);
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick, 1), "#", '#', BPBlocks.basalt_brick_small);
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick, 1), "#", '#', BPBlocks.marble_brick_small);

        craftManager.addRecipe(new ItemStack(BPBlocks.fancy_basalt, 1), "#", '#', BPBlocks.basalt_brick);
        craftManager.addRecipe(new ItemStack(BPBlocks.fancy_marble, 1), "#", '#', BPBlocks.marble_brick);
        craftManager.addRecipe(new ItemStack(BPItems.amethyst, 9), "#", '#', BPBlocks.amethyst_block);
        craftManager.addRecipe(new ItemStack(BPItems.sapphire, 9), "#", '#', BPBlocks.sapphire_block);
        craftManager.addRecipe(new ItemStack(BPItems.ruby, 9), "#", '#', BPBlocks.ruby_block);
        craftManager.addRecipe(new ItemStack(BPItems.silver_ingot, 9), "#", '#', BPBlocks.silver_block);
        craftManager.addRecipe(new ItemStack(BPItems.copper_ingot, 9), "#", '#', BPBlocks.copper_block);
        craftManager.addRecipe(new ItemStack(BPItems.zinc_ingot, 9), "#", '#', BPBlocks.zinc_block);
        craftManager.addRecipe(new ItemStack(BPItems.tungsten_ingot, 9), "#", '#', BPBlocks.tungsten_block);
        craftManager.addRecipe(new ItemStack(BPItems.teslatite, 9), "#", '#', BPBlocks.teslatite_block);

        if (Config.useAltScrewdriverRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.screwdriver_handle), "#", '#', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.screwdriver, 1), "# ", " S", '#', "ingotIron", 'S',
                    BPItems.screwdriver_handle));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.screwdriver, 1), "# ", " S", '#', "ingotIron", 'S', "stickWood"));
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.athame, 1), "# ", " S", '#', "ingotSilver", 'S', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_wire, 1), "#", "W", '#', "dustTeslatite", 'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_anode, 3), " # ", "###", "WWW", '#', "dustTeslatite", 'W',
                BPItems.stone_tile));
        craftManager.addRecipe(new ItemStack(BPItems.stone_cathode, 1), "#", "W", '#', Blocks.redstone_torch, 'W', BPItems.stone_tile);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_pointer, 1), "S", "#", "W", 'S', Items.quartz, '#',
                Blocks.redstone_torch, 'W', BPItems.stone_tile));
        craftManager.addRecipe(new ItemStack(BPItems.silicon_chip, 1), " # ", "WWW", '#', BPItems.red_doped_wafer, 'W', BPItems.stone_tile);
        // GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.plate_assembly, 1), " # ", "SRS", "#C#", '#', BPItems.stone_tile, 'S',
        // "stickWood", 'R', BPItems.red_alloy_ingot, 'C', BPItems.stone_cathode));
        craftManager.addShapelessRecipe(new ItemStack(BPItems.taintedsilicon_chip, 1), BPItems.silicon_chip, Items.glowstone_dust);
        // TODO: stone_redwire recipe and stone_bundle recipe

        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("and"), "ACA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("buffer"), "ACA", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("bustransceiver"), "BBB", "N#N", "BBB", '#', BPItems.stone_tile, 'B',
        // BPItems.stone_bundle, 'N', BPItems.red_doped_wafer);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("counter"), "#W#", "CPC", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("invert"), "#R#", "ROR", "#R#", '#', BPItems.stone_tile, 'O',
        // BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("lightCell"), "#W#", "#B#", "###", '#', BPItems.stone_tile, 'B',
                BPItems.blue_doped_wafer, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("multiplexer"), "ACA", "C#C", "ACW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("nand"), "AAA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("noninvert"), "#R#", "ROR", "#RC", '#', BPItems.stone_tile, 'C',
        // BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("nor"), "#A#", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("not"), "#A#", "ACA", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("null"), "#R#", "RRR", "#R#", '#', BPItems.stone_tile, 'R',
        // BPItems.stone_redwire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("or"), "#C#", "WCW", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("randomizer"), "#T#", "WWW", "TWT", '#', BPItems.stone_tile, 'T',
                BPItems.taintedsilicon_chip, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("pulseformer"), "ACA", "CAC", "WW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("repeater"), "#CW", "#AW", "#WC", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("rs"), "WWA", "C#C", "AWW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("sequencer"), "#C#", "CPC", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'P', BPItems.stone_pointer);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("state"), "#AC", "WXP", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'P', BPItems.stone_pointer, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("synchronizer"), "WCW", "XAX", "WWW", '#', 'A', BPItems.stone_anode, 'C',
        // BPItems.stone_cathode, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("timer"), "#W#", "WPW", "ACA", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("toggle"), "#C#", "WLW", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'L', Blocks.lever);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("transparent"), "ACW", "CCC", "CW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("xnor"), "ACA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("xor"), "AWA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);

        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("pneumaticTube", 8), "BGB", 'B', "ingotBrass", 'G',
                "blockGlass"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(PartRegistry.getInstance().getItemForPart("restrictionTube", 1), "ingotIron", PartRegistry
                .getInstance().getItemForPart("pneumaticTube")));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("magTube", 8), "CCC", "BGB", "CCC", 'B',
                Blocks.obsidian, 'G', "blockGlass", 'C', BPItems.copper_wire));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("accelerator", 1), "OIO", "I I", "OIO", 'O',
                Blocks.obsidian, 'I', BPItems.blue_alloy_ingot));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("accelerator", 1), "IOI", "O O", "IOI", 'O',
                Blocks.obsidian, 'I', BPItems.blue_alloy_ingot));
        craftManager.addRecipe(new ItemStack(BPBlocks.alloy_furnace, 1), "###", "# #", "###", '#', Blocks.brick_block);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.project_table), "SSS", "WCW", "WIW", 'S', "stone", 'W', "plankWood", 'C',
                Blocks.crafting_table, 'I', Blocks.chest));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.block_breaker, 1), "#A#", "#P#", "#R#", '#', "cobblestone", 'A',
                Items.iron_pickaxe, 'P', Blocks.piston, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.buffer, 1), "#P#", "P P", "#P#", '#', Blocks.iron_bars, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.ejector, 1), "PBP", "PTP", "#R#", '#', "cobblestone", 'P', "plankWood",
                'B', BPBlocks.buffer, 'T', BPBlocks.transposer, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.relay, 1), "PBP", "PTP", "#W#", '#', "cobblestone", 'P', "plankWood", 'B',
                BPBlocks.buffer, 'T', BPBlocks.transposer, 'W', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.transposer, 1), "###", "WPW", "#R#", '#', "cobblestone", 'P',
                Blocks.piston, 'R', "dustRedstone", 'W', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.item_detector, 1), "ITI", "WUW", "PTP", 'I', "ingotBrass", 'T',
                PartRegistry.getInstance().getItemForPart("pneumaticTube"), 'W', BPItems.red_doped_wafer, 'U', Blocks.wooden_pressure_plate, 'P',
                "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.regulator, 1), "IBI", "WDW", "PBP", 'I', "ingotBrass", 'B',
                BPBlocks.buffer, 'W', BPItems.red_doped_wafer, 'D', BPBlocks.item_detector, 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.retriever, 1), "BLB", "EFE", "IAI", 'B', "ingotBrass", 'L', Items.leather,
                'E', Items.ender_pearl, 'F', BPBlocks.filter, 'I', "ingotIron", 'A', "ingotBlueAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.filter, 1), "CCC", "GPG", "CWC", 'C', "cobblestone", 'G', "ingotGold", 'P',
                Blocks.piston, 'W', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.sorting_machine, 1), "CCC", "GPG", "CWC", 'C', "ingotIron", 'G',
                BPItems.red_doped_wafer, 'P', BPBlocks.filter, 'W', BPItems.blue_alloy_ingot));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.igniter, 1), "NFN", "CDC", "CRC", 'C', "cobblestone", 'N',
                Blocks.netherrack, 'F', Items.flint_and_steel, 'R', "dustRedstone", 'D', BPBlocks.deployer));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.deployer, 1), "CIC", "CPC", "CRC", 'C', "cobblestone", 'I', Blocks.chest,
                'P', Blocks.piston, 'R', "dustRedstone"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.canvas, 1), "SSS", "SiS", "SSS", 'S', Items.string, 'i', "stickWood"));
        craftManager.addRecipe(new ItemStack(BPItems.seedBag, 1), " S ", "C C", "CCC", 'S', Items.string, 'C', BPItems.canvas);
        craftManager.addRecipe(new ItemStack(BPItems.canvas_bag, 1, 15), "SSS", "S S", "SSS", 'S', BPItems.canvas);

        registerCanvasBagRecipes(new ItemStack(BPItems.canvas_bag));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.copper_wire, 1), BPItems.diamond_drawplate, "ingotCopper"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.iron_wire, 1), BPItems.diamond_drawplate, "ingotIron"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.string, 4, 0), new ItemStack(BPItems.wool_card, 1,
                OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.wool_card, 1, 0), "f", "p", "s", 'f', BPItems.iron_wire, 'p', "plankWood",
                's', "stickWood"));

        for (int i = 0; i < Refs.oreDictDyes.length; i++) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.lumar, 2, 15 - i), new ItemStack(Items.redstone, 1), new ItemStack(
                    Items.glowstone_dust, 1), Refs.oreDictDyes[i], Refs.oreDictDyes[i]));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLamp[i], 1), "gLg", "gLg", "gRg", 'g', "paneGlassColorless", 'L',
                    new ItemStack(BPItems.lumar, 1, 15 - i), 'R', "dustRedstone"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.blockLampInverted[i], 1), "gLg", "gLg", "gRg", 'g',
                    "paneGlassColorless", 'L', new ItemStack(BPItems.lumar, 1, 15 - i), 'R', Blocks.redstone_torch));
            // other multipart lamps
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

            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.paint_brush, 1, i), new ItemStack(BPItems.paint_can, 1, i), new ItemStack(
                    BPItems.paint_brush, 1, OreDictionary.WILDCARD_VALUE));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.paint_can, 1, i), new ItemStack(BPItems.flax_seed), new ItemStack(
                    BPItems.flax_seed), Refs.oreDictDyes[i], new ItemStack(BPItems.paint_can, 1, 16)));
        }

        craftManager.addRecipe(new ItemStack(BPItems.paint_can, 1, 16), "t t", "t t", "ttt", 't', BPItems.zincplate);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.paint_brush, 1, 16), " w", "s ", 'w', Blocks.wool, 's', "stickWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire", 12), "iii", 'i',
                BPItems.blue_alloy_ingot));

        if (Loader.isModLoaded("ForgeMicroblock")) {
            ItemStack diamondPanel = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:microblock"), 1, 2);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockDiamond");
            diamondPanel.setTagCompound(tag);

            ItemStack ironStrip = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:microblock"), 1, 769);
            tag = new NBTTagCompound();
            tag.setString("mat", "tile.blockIron");
            ironStrip.setTagCompound(tag);

            craftManager.addRecipe(new ItemStack(BPItems.diamond_drawplate), " i ", "idi", " i ", 'i', ironStrip, 'd', diamondPanel);

            ItemStack diamondSawFMP = new ItemStack(GameData.getItemRegistry().getObject("ForgeMicroblock:sawDiamond"), 1,
                    OreDictionary.WILDCARD_VALUE);
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), diamondSawFMP, new ItemStack(BPItems.silicon_boule));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.diamond_drawplate), "IDI", "IDI", "IDI", 'I', "ingotIron", 'D',
                    "gemDiamond"));
            GameRegistry.addShapelessRecipe(new ItemStack(BPItems.silicon_wafer, 16), new ItemStack(BPItems.diamond_saw, 1,
                    OreDictionary.WILDCARD_VALUE), new ItemStack(BPItems.silicon_boule));
        }

        // Alloy furnace
        IAlloyFurnaceRegistry af = BPApi.getInstance().getAlloyFurnaceRegistry();
        // af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.redstone, 4), Items.iron_ingot);
        af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.redstone, 4), BPItems.copper_ingot);
        af.addRecipe(new ItemStack(BPItems.brass_ingot, 4), new ItemStack(BPItems.copper_ingot, 3), BPItems.zinc_ingot);
        af.addRecipe(new ItemStack(BPItems.blue_alloy_ingot, 1), new ItemStack(BPItems.teslatite, 4), BPItems.silver_ingot);
        af.addRecipe(new ItemStack(BPItems.silicon_boule, 1), new ItemStack(Items.coal, 8), new ItemStack(Blocks.sand, 8));

        af.addRecipe(new ItemStack(BPItems.red_doped_wafer, 1), new ItemStack(Items.redstone, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.blue_doped_wafer, 1), new ItemStack(BPItems.teslatite, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.zincplate, 4), new ItemStack(BPItems.zinc_ingot, 1), new ItemStack(Items.iron_ingot, 2));

        af.addRecyclingRecipe(new ItemStack(Blocks.iron_block));
        af.addRecyclingRecipe(new ItemStack(Blocks.gold_block));
        af.addRecyclingRecipe(new ItemStack(Items.iron_ingot));
        af.addRecyclingRecipe(new ItemStack(Items.gold_ingot));
        af.addRecyclingRecipe(new ItemStack(Items.gold_nugget));
    }

    private static void registerCanvasBagRecipes(ItemStack is) {

        RecipeSorter.register(Refs.MODID + ":canvasBag", CanvasBagRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        for (int damage = Refs.oreDictDyes.length - 1; damage >= 0; damage--) {
            ItemStack input = is.copy();
            input.setItemDamage(damage);

            for (int dye = 0; dye < Refs.oreDictDyes.length; dye++) {
                if (15 - dye != input.getItemDamage()) {
                    ItemStack output = is.copy();
                    output.setItemDamage(dye);
                    GameRegistry.addRecipe(new CanvasBagRecipe(output, input, Refs.oreDictDyes[dye]));
                }
            }
        }
    }
}
