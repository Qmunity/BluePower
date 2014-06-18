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

package net.quetzi.bluepower.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.api.BPRegistry;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRegistry;
import net.quetzi.bluepower.recipe.CanvasBagRecipe;
import net.quetzi.bluepower.references.Refs;

public class Recipes {
    
    public static void init(CraftingManager craftManager) {
    
        GameRegistry.addSmelting(BPBlocks.basalt_cobble, new ItemStack(BPBlocks.basalt), 0);
        GameRegistry.addSmelting(BPBlocks.copper_ore, new ItemStack(BPItems.copper_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.tin_ore, new ItemStack(BPItems.tin_ingot), 0.5F);
        GameRegistry.addSmelting(BPBlocks.silver_ore, new ItemStack(BPItems.silver_ingot), 0.7F);
        GameRegistry.addSmelting(BPBlocks.tungsten_block, new ItemStack(BPItems.tungsten_ingot), 0.8F);
        GameRegistry.addSmelting(BPBlocks.basalt, new ItemStack(BPBlocks.basalt_tile), 0);
        GameRegistry.addSmelting(BPBlocks.marble, new ItemStack(BPBlocks.marble_tile), 0);
        GameRegistry.addSmelting(Blocks.stone, new ItemStack(BPItems.stone_wafer, 2), 0);
        
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick, 4), new Object[] { "##", "##", '#', BPBlocks.basalt });
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick, 4), new Object[] { "##", "##", '#', BPBlocks.marble });
        craftManager.addRecipe(new ItemStack(BPBlocks.copper_block, 1), new Object[] { "###", "###", "###", '#', BPItems.copper_ingot });
        craftManager.addRecipe(new ItemStack(BPBlocks.silver_block, 1), new Object[] { "###", "###", "###", '#', BPItems.silver_ingot });
        craftManager.addRecipe(new ItemStack(BPBlocks.tin_block, 1), new Object[] { "###", "###", "###", '#', BPItems.tin_ingot });
        craftManager.addRecipe(new ItemStack(BPBlocks.tungsten_block, 1), new Object[] { "###", "###","###", '#', BPItems.tungsten_ingot });
        
        craftManager.addRecipe(new ItemStack(BPItems.ruby_axe, 1), new Object[] { "GG ", "GS ", " S ", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_axe, 1), new Object[] { " GG", " SG", " S ", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_pickaxe, 1), new Object[] { "GGG", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_sword, 1), new Object[] { "G", "G", "S", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_spade, 1), new Object[] { "G", "S", "S", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_hoe, 1), new Object[] { "GG ", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_hoe, 1), new Object[] { " GG", " S ", " S ", 'G', BPItems.ruby, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', BPItems.ruby, 'S', Items.stick });
        
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_axe, 1), new Object[] { "GG ", "GS ", " S ", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_axe, 1), new Object[] { " GG", " SG", " S ", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_pickaxe, 1), new Object[] { "GGG", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_sword, 1), new Object[] { "G", "G", "S", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_spade, 1), new Object[] { "G", "S", "S", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_hoe, 1), new Object[] { "GG ", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_hoe, 1), new Object[] { " GG", " S ", " S ", 'G', BPItems.sapphire, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', BPItems.sapphire, 'S', Items.stick });
        
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_axe, 1), new Object[] { "GG ", "GS ", " S ", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_axe, 1), new Object[] { " GG", " SG", " S ", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_pickaxe, 1), new Object[] { "GGG", " S ", " S ", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_sword, 1), new Object[] { "G", "G", "S", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_spade, 1), new Object[] { "G", "S", "S", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_hoe, 1), new Object[] { "GG ", " S ", " S ", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_hoe, 1), new Object[] { " GG", " S ", " S ", 'G', BPItems.amethyst, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', BPItems.amethyst, 'S', Items.stick });
        
        craftManager.addRecipe(new ItemStack(BPItems.wood_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', Blocks.planks, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.stone_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', Blocks.cobblestone, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.iron_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', Items.iron_ingot, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.gold_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', Items.gold_ingot, 'S', Items.stick });
        craftManager.addRecipe(new ItemStack(BPItems.diamond_sickle, 1), new Object[] { " G ", "  G", "SG ", 'G', Items.diamond, 'S', Items.stick });
        
        craftManager.addRecipe(new ItemStack(BPItems.iron_saw, 1), new Object[] { "SSS", " II", " II", 'S', Items.stick, 'I', Items.iron_ingot });
        craftManager.addRecipe(new ItemStack(BPItems.ruby_saw, 1), new Object[] { "SSS", " II", " ##", 'S', Items.stick, 'I', Items.iron_ingot, '#', BPItems.ruby });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst_saw, 1), new Object[] { "SSS", " II", " ##", 'S', Items.stick, 'I', Items.iron_ingot, '#', BPItems.amethyst });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire_saw, 1), new Object[] { "SSS", " II", " ##", 'S', Items.stick, 'I', Items.iron_ingot, '#', BPItems.sapphire });
        craftManager.addRecipe(new ItemStack(BPItems.diamond_saw, 1), new Object[] { "SSS", " II", " ##", 'S', Items.stick, 'I', Items.iron_ingot, '#', Items.diamond });
        
        craftManager.addRecipe(new ItemStack(BPItems.indigo_dye, 1), new Object[] { "#", '#', BPBlocks.indigo_flower });
        
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick_small, 4), new Object[] { "##", "##", '#', BPBlocks.basalt_brick });
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick_small, 4), new Object[] { "##", "##", '#', BPBlocks.marble_brick });
        craftManager.addRecipe(new ItemStack(BPBlocks.basalt_brick, 1), new Object[] { "#", '#', BPBlocks.basalt_brick_small });
        craftManager.addRecipe(new ItemStack(BPBlocks.marble_brick, 1), new Object[] { "#", '#', BPBlocks.marble_brick_small });
        
        craftManager.addRecipe(new ItemStack(BPBlocks.fancy_basalt, 1), new Object[] { "#", '#', BPBlocks.basalt_brick });
        craftManager.addRecipe(new ItemStack(BPBlocks.fancy_marble, 1), new Object[] { "#", '#', BPBlocks.marble_brick });
        craftManager.addRecipe(new ItemStack(BPItems.amethyst, 9), new Object[] { "#", '#', BPBlocks.amethyst_block });
        craftManager.addRecipe(new ItemStack(BPItems.sapphire, 9), new Object[] { "#", '#', BPBlocks.sapphire_block });
        craftManager.addRecipe(new ItemStack(BPItems.ruby, 9), new Object[] { "#", '#', BPBlocks.ruby_block });
        craftManager.addRecipe(new ItemStack(BPItems.silver_ingot, 9), new Object[] { "#", '#', BPBlocks.silver_block });
        craftManager.addRecipe(new ItemStack(BPItems.copper_ingot, 9), new Object[] { "#", '#', BPBlocks.copper_block });
        craftManager.addRecipe(new ItemStack(BPItems.tin_ingot, 9), new Object[] { "#", '#', BPBlocks.tin_block });
        craftManager.addRecipe(new ItemStack(BPItems.tungsten_ingot, 9), new Object[] { " # ", '#', BPBlocks.tungsten_block });
        
        if(Config.useAltScrewdriverRecipe) {
            craftManager.addRecipe(new ItemStack(BPItems.screwdriver_handle), new Object[] {"#", '#', Items.stick});
            craftManager.addRecipe(new ItemStack(BPItems.screwdriver, 1), new Object[] { "# ", " S", '#', Items.iron_ingot, 'S', BPItems.screwdriver_handle });
        } else {
            craftManager.addRecipe(new ItemStack(BPItems.screwdriver, 1), new Object[] { "# ", " S", '#', Items.iron_ingot, 'S', Items.stick });
        }


        craftManager.addRecipe(new ItemStack(BPItems.athame, 1), new Object[] { "# ", " S", '#', BPItems.silver_ingot, 'S', Items.stick });

        craftManager.addRecipe(new ItemStack(BPItems.stone_wire, 1), new Object[] {"#", "W", '#', Items.redstone, 'W', BPItems.stone_wafer });
        craftManager.addRecipe(new ItemStack(BPItems.stone_anode, 3), new Object[] {" # ", "###", "WWW", '#', Items.redstone, 'W', BPItems.stone_wafer });
        craftManager.addRecipe(new ItemStack(BPItems.stone_cathode, 1), new Object[] {"#", "W", '#', Blocks.redstone_torch, 'W', BPItems.stone_wafer });
        craftManager.addRecipe(new ItemStack(BPItems.stone_pointer, 1), new Object[] {"S", "#", "W",'S', Blocks.stone, '#', Blocks.redstone_torch, 'W', BPItems.stone_wafer });
        craftManager.addRecipe(new ItemStack(BPItems.silicon_chip, 1), new Object[] {" # ", "WWW", '#', BPItems.red_doped_wafer, 'W', BPItems.stone_wafer });
        craftManager.addRecipe(new ItemStack(BPItems.plate_assembly, 1), new Object[] {" # ", "SRS", "#C#", '#', BPItems.stone_wafer, 'S', Items.stick, 'R', BPItems.red_alloy_ingot, 'C', BPItems.stone_cathode});
        craftManager.addShapelessRecipe(new ItemStack(BPItems.taintedsilicon_chip, 1), new Object[] {BPItems.silicon_chip, Items.glowstone_dust});
//        TODO: stone_redwire recipe and stone_bundle recipe

        craftManager.addRecipe(PartRegistry.getItemForPart("and"), new Object[] {"ACA", "CCC", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
        craftManager.addRecipe(PartRegistry.getItemForPart("buffer"), new Object[] {"ACA", "WCW", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("bustransceiver"), new Object[] {"BBB", "N#N", "BBB", '#', BPItems.stone_wafer, 'B', BPItems.stone_bundle, 'N', BPItems.red_doped_wafer});
        craftManager.addRecipe(PartRegistry.getItemForPart("counter"), new Object[] {"#W#", "CPC", "#W#", '#', BPItems.stone_wafer, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer});
//        craftManager.addRecipe(PartRegistry.getItemForPart("invert"), new Object[] {"#R#", "ROR", "#R#", '#', BPItems.stone_wafer, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("light"), new Object[] {"#W#", "#B#", "###", '#', BPItems.stone_wafer, 'B', BPItems.blue_doped_wafer, 'W', BPItems.stone_wire});
        craftManager.addRecipe(PartRegistry.getItemForPart("multiplexer"), new Object[] {"ACA", "C#C", "ACW", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("nand"), new Object[] {"AAA", "CCC", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("noninvert"), new Object[] {"#R#", "ROR", "#RC", '#', BPItems.stone_wafer, 'C', BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("nor"), new Object[] {"#A#", "WCW", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
        craftManager.addRecipe(PartRegistry.getItemForPart("not"), new Object[] {"#A#", "ACA", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("null"), new Object[] {"#R#", "RRR", "#R#", '#', BPItems.stone_wafer, 'R', BPItems.stone_redwire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("or"), new Object[] {"#C#", "WCW", "#W#", '#', BPItems.stone_wafer, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("pulseformer"), new Object[] {"ACA", "CAC", "WW#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("randomizer"), new Object[] {"#T#", "WWW", "TWT", '#', BPItems.stone_wafer, 'T', BPItems.taintedsilicon_chip, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("repeater"), new Object[] {"#CW", "#AW", "#WC", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("rs"), new Object[] {"WWA", "C#C", "AWW", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
        craftManager.addRecipe(PartRegistry.getItemForPart("sequencer"), new Object[] {"#C#", "CPC", "#C#", '#', BPItems.stone_wafer, 'C', BPItems.stone_cathode, 'P', BPItems.stone_pointer});
//        craftManager.addRecipe(PartRegistry.getItemForPart("state"), new Object[] {"#AC", "WXP", "#W#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'P', BPItems.stone_pointer, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip});
//        craftManager.addRecipe(PartRegistry.getItemForPart("synchronizer"), new Object[] {"WCW", "XAX", "WWW", '#', 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip});
        craftManager.addRecipe(PartRegistry.getItemForPart("timer"), new Object[] {"#W#", "WPW", "ACA", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer});
//        craftManager.addRecipe(PartRegistry.getItemForPart("toggle"), new Object[] {"#C#", "WLW", "#C#", '#', BPItems.stone_wafer, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'L', Blocks.lever});
//        craftManager.addRecipe(PartRegistry.getItemForPart("transparent"), new Object[] {"ACW", "CCC", "CW#", '#', BPItems.stone_wafer, 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("xnor"), new Object[] {"ACA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});
//        craftManager.addRecipe(PartRegistry.getItemForPart("xor"), new Object[] {"AWA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire});

        craftManager.addRecipe(new ItemStack(BPBlocks.alloy_furnace, 1), new Object[] {"###", "# #", "###", '#', Blocks.brick_block});
        craftManager.addRecipe(new ItemStack(BPBlocks.block_breaker, 1), new Object[] {"#A#", "#P#", "#R#", '#', Blocks.cobblestone, 'A', Items.iron_pickaxe, 'P', Blocks.piston, 'R', Items.redstone});
        craftManager.addRecipe(new ItemStack(BPItems.canvas, 1), new Object[] {"SSS", "S S", "SSS", 'S', Items.string});
        craftManager.addRecipe(new ItemStack(BPItems.seedBag, 1), new Object[] {" S ", "C C", "CCC", 'S', Items.string, 'C', BPItems.canvas });
        craftManager.addRecipe(new ItemStack(BPItems.canvas_bag, 1, 0), new Object[] {"CCC", "C C", "CCC", 'C', BPItems.canvas_bag});
        
        for (int i = 1; i < 16; i++) {
            craftManager.addRecipe(new ItemStack(BPItems.canvas_bag, 1, i), new Object[] {"CCC", "CDC", "CCC", 'C', BPItems.canvas, 'D', OreDictionary.getOreID(Refs.oreDictDyes[i])});
        }
        
        GameRegistry.addRecipe(new CanvasBagRecipe());
        
        // Alloy furnace
        IAlloyFurnaceRegistry af = BPRegistry.alloyFurnaceRegistry;
        af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.redstone, 4), Items.iron_ingot);
        af.addRecipe(new ItemStack(BPItems.red_alloy_ingot, 1), new ItemStack(Items.redstone, 4), BPItems.copper_ingot);
        af.addRecipe(new ItemStack(BPItems.brass_ingot, 4), new ItemStack(BPItems.copper_ingot, 3), BPItems.tin_ingot);
        af.addRecipe(new ItemStack(BPItems.blue_alloy_ingot, 1), new ItemStack(BPItems.nikolite, 4), BPItems.silver_ingot);
        af.addRecipe(new ItemStack(BPItems.silicon_boule, 1), new ItemStack(Items.coal, 8), new ItemStack(Blocks.sand, 8));
        
        af.addRecipe(new ItemStack(BPItems.red_doped_wafer, 1), new ItemStack(Items.redstone, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.blue_doped_wafer, 1), new ItemStack(BPItems.nikolite, 4), BPItems.silicon_wafer);
        af.addRecipe(new ItemStack(BPItems.tinplate, 4), new ItemStack(BPItems.tin_ingot, 1), new ItemStack(Items.iron_ingot, 2));
        
        af.addRecyclingRecipe(new ItemStack(Blocks.iron_block));
        af.addRecyclingRecipe(new ItemStack(Blocks.gold_block));
        af.addRecyclingRecipe(new ItemStack(Items.iron_ingot));
        af.addRecyclingRecipe(new ItemStack(Items.gold_ingot));
        
    }
}
