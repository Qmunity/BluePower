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
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by Quetzi on 27/10/14.
 */
public class LogicRecipes {

    public static void init() {

        // Components
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_wire, 1), "#", "W", '#', "dustInfusedteslatite", 'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_anode, 3), " # ", "###", "WWW", '#', "dustInfusedteslatite", 'W',
                BPItems.stone_tile));
        GameRegistry.addRecipe(new ItemStack(BPItems.stone_cathode, 1), "#", "W", '#', Blocks.redstone_torch, 'W', BPItems.stone_tile);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_pointer, 1), "S", "#", "W", 'S', "stone", '#', Blocks.redstone_torch,
                'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ItemStack(BPItems.silicon_chip, 1), " # ", "WWW", '#', BPItems.red_doped_wafer, 'W', BPItems.stone_tile);
        // GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.plate_assembly, 1), " # ", "SRS", "#C#", '#', BPItems.stone_tile, 'S',
        // "stickWood", 'R', BPItems.red_alloy_ingot, 'C', BPItems.stone_cathode));
        GameRegistry.addShapelessRecipe(new ItemStack(BPItems.taintedsilicon_chip, 1), BPItems.silicon_chip, Items.glowstone_dust);
        GameRegistry.addShapelessRecipe(new ItemStack(BPItems.infused_teslatite_dust, 2), BPItems.teslatite_dust, Items.redstone);
        // TODO: stone_redwire recipe and stone_bundle recipe

        // Wires

        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bluestoneWire").getItem(12), "iii", 'i', "ingotRedAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bluestoneWire").getItem(12), "i", "i", "i", 'i', "ingotRedAlloy"));

        // Insulated wire
        /*
        // TODO: Wire recipes
        {
            int i = 15;
            for (String s : ItemDye.field_150921_b) {
                GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bluestoneWire." + s, 12), "www", "iii", "www",
                        'i', "ingotRedAlloy", 'w', new ItemStack(Blocks.wool, 1, i)));
                GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bluestoneWire." + s, 12), "wiw", "wiw", "wiw",
                        'i', "ingotRedAlloy", 'w', new ItemStack(Blocks.wool, 1, i)));
                i--;
            }
        }
        // Bundled wire
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bluestoneWire.bundled", 2), "sws", "www", "sws",
                    'w', "bluestoneInsulated", 's', new ItemStack(Items.string, 1)));

*/
        // Blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_table), "WIW", "WCW", "WPW", 'W', "plankWood", 'I', PartManager
                .getPartInfo("integratedCircuit3x3").getItem(), 'C', Blocks.chest, 'P', BPBlocks.project_table));

        // Gates and Circuits
        GameRegistry.addRecipe(PartManager.getPartInfo("and").getItem(), "ACA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("buffer").getItem(), "ACA", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartManager.getPartInfo("bustransceiver"), "BBB", "N#N", "BBB", '#', BPItems.stone_tile, 'B',
        // BPItems.stone_bundle, 'N', BPItems.red_doped_wafer);
        GameRegistry.addRecipe(PartManager.getPartInfo("counter").getItem(), "#W#", "CPC", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        // craftManager.addRecipe(PartManager.getPartInfo("invert"), "#R#", "ROR", "#R#", '#', BPItems.stone_tile, 'O',
        // BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("lightCell").getItem(), "#W#", "#B#", "###", '#', BPItems.stone_tile, 'B',
                BPItems.blue_doped_wafer, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("multiplexer").getItem(), "ACA", "C#C", "ACW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("nand").getItem(), "AAA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartManager.getPartInfo("noninvert"), "#R#", "ROR", "#RC", '#', BPItems.stone_tile, 'C',
        // BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("nor").getItem(), "#A#", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("not").getItem(), "#A#", "ACA", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartManager.getPartInfo("null"), "#R#", "RRR", "#R#", '#', BPItems.stone_tile, 'R',
        // BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("or").getItem(), "#C#", "WCW", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("randomizer").getItem(), "#T#", "WWW", "TWT", '#', BPItems.stone_tile, 'T',
                BPItems.taintedsilicon_chip, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("pulseformer").getItem(), "ACA", "CAC", "WW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("repeater").getItem(), "#CW", "#AW", "#WC", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("rs").getItem(), "WWA", "C#C", "AWW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("sequencer").getItem(), "#C#", "CPC", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'P', BPItems.stone_pointer);
        GameRegistry.addRecipe(PartManager.getPartInfo("state").getItem(), "#AC", "WXP", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'P', BPItems.stone_pointer, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        GameRegistry.addRecipe(PartManager.getPartInfo("synchronizer").getItem(), "WCW", "XAX", "WWW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        GameRegistry.addRecipe(PartManager.getPartInfo("timer").getItem(), "#W#", "WPW", "ACA", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        GameRegistry.addRecipe(PartManager.getPartInfo("toggle").getItem(), "#C#", "WLW", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'L', Blocks.lever);
        GameRegistry.addRecipe(PartManager.getPartInfo("transparent").getItem(), "ACW", "CCC", "CW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("xnor").getItem(), "ACA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("xor").getItem(), "AWA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit3x3").getItem(), "TST", "SSS", "TST", 'T', BPItems.stone_tile, 'S',
                BPItems.silicon_chip);
        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit5x5").getItem(), "II", "II", 'I', PartManager.getPartInfo(
                "integratedCircuit3x3").getItem());
        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit7x7").getItem(), "II", "II", 'I', PartManager.getPartInfo(
                "integratedCircuit5x5").getItem());
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_database, 1), "#C#", "BTB", "###", '#', "ingotIron", 'C',
                PartManager.getPartInfo("integratedCircuit7x7").getItem(), 'B', Blocks.bookshelf, 'T', BPBlocks.circuit_table));

    }
}
