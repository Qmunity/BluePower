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
import com.bluepowermod.part.PartRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
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
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire", 12), "iii", 'i', "ingotBlueAlloy"));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire", 12), "i", "i", "i", 'i', "ingotBlueAlloy"));
        // Insulated wire
        {
            int i = 15;
            for (String s : ItemDye.field_150921_b) {
                GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire." + s, 12), "www", "iii", "www",
                        'i', "ingotBlueAlloy", 'w', new ItemStack(Blocks.wool, 1, i)));
                GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire." + s, 12), "wiw", "wiw", "wiw",
                        'i', "ingotBlueAlloy", 'w', new ItemStack(Blocks.wool, 1, i)));
                i--;
            }
        }
        // Bundled wire
        GameRegistry.addRecipe(new ShapedOreRecipe(PartRegistry.getInstance().getItemForPart("bluestoneWire.bundled", 2), "sws", "www", "sws",
                    'w', "bluestoneInsulated", 's', new ItemStack(Items.string, 1)));

        // Blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_table), "WIW", "WCW", "WPW", 'W', "plankWood", 'I', PartRegistry
                .getInstance().getItemForPart("integratedCircuit3x3"), 'C', Blocks.chest, 'P', BPBlocks.project_table));

        // Gates and Circuits
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("and"), "ACA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("buffer"), "ACA", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("bustransceiver"), "BBB", "N#N", "BBB", '#', BPItems.stone_tile, 'B',
        // BPItems.stone_bundle, 'N', BPItems.red_doped_wafer);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("counter"), "#W#", "CPC", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("invert"), "#R#", "ROR", "#R#", '#', BPItems.stone_tile, 'O',
        // BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("lightCell"), "#W#", "#B#", "###", '#', BPItems.stone_tile, 'B',
                BPItems.blue_doped_wafer, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("multiplexer"), "ACA", "C#C", "ACW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("nand"), "AAA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("noninvert"), "#R#", "ROR", "#RC", '#', BPItems.stone_tile, 'C',
        // BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("nor"), "#A#", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("not"), "#A#", "ACA", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        // craftManager.addRecipe(PartRegistry.getInstance().getItemForPart("null"), "#R#", "RRR", "#R#", '#', BPItems.stone_tile, 'R',
        // BPItems.stone_redwire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("or"), "#C#", "WCW", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("randomizer"), "#T#", "WWW", "TWT", '#', BPItems.stone_tile, 'T',
                BPItems.taintedsilicon_chip, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("pulseformer"), "ACA", "CAC", "WW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("repeater"), "#CW", "#AW", "#WC", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("rs"), "WWA", "C#C", "AWW", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("sequencer"), "#C#", "CPC", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'P', BPItems.stone_pointer);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("state"), "#AC", "WXP", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'P', BPItems.stone_pointer, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("synchronizer"), "WCW", "XAX", "WWW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'X', BPItems.silicon_chip);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("timer"), "#W#", "WPW", "ACA", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire, 'P', BPItems.stone_pointer);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("toggle"), "#C#", "WLW", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire, 'L', Blocks.lever);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("transparent"), "ACW", "CCC", "CW#", '#', BPItems.stone_tile, 'A',
                BPItems.stone_anode, 'C', BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("xnor"), "ACA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("xor"), "AWA", "CAC", "WCW", 'A', BPItems.stone_anode, 'C',
                BPItems.stone_cathode, 'W', BPItems.stone_wire);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("integratedCircuit3x3"), "TST", "SSS", "TST", 'T', BPItems.stone_tile, 'S',
                BPItems.silicon_chip);
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("integratedCircuit5x5"), "II", "II", 'I', PartRegistry.getInstance()
                .getItemForPart("integratedCircuit3x3"));
        GameRegistry.addRecipe(PartRegistry.getInstance().getItemForPart("integratedCircuit7x7"), "II", "II", 'I', PartRegistry.getInstance()
                .getItemForPart("integratedCircuit5x5"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_database, 1), "#C#", "BTB", "###", '#', "ingotIron", 'C',
                PartRegistry.getInstance().getItemForPart("integratedCircuit7x7"), 'B', Blocks.bookshelf, 'T', BPBlocks.circuit_table));
    }
}
