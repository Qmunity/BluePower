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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.commons.lang3.StringUtils;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartManager;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Quetzi on 27/10/14.
 */
public class LogicRecipes {

    public static void init() {

        // Components
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_wire_tile, 1), "#", "W", '#', "dustTeslatite", 'W',
                BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_anode_tile, 3), " # ", "###", "WWW", '#',
                "dustTeslatite", 'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_cathode_tile, 1), "T#T", " W ", 'T', "dustTeslatite",
                '#', Blocks.redstone_torch, 'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_pointer_tile, 1), " S ", "T#T", " W ", 'S', "stone",
                '#', Blocks.redstone_torch, 'W', BPItems.stone_tile, 'T', "dustTeslatite"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_wire_tile, 1), "#", "W", '#', "dustRedstone", 'W',
                BPItems.stone_tile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_anode_tile, 3), " # ", "###", "WWW", '#', "dustRedstone",
                'W', BPItems.stone_tile));
        GameRegistry.addRecipe(new ItemStack(BPItems.redstone_cathode_tile, 1), "#", "W", '#', Blocks.redstone_torch, 'W',
                BPItems.stone_tile);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_pointer_tile, 1), "S", "#", "W", 'S', "stone", '#',
                Blocks.redstone_torch, 'W', BPItems.stone_tile));

        GameRegistry.addRecipe(new ItemStack(BPItems.silicon_chip_tile, 1), " # ", "WWW", '#', BPItems.blue_doped_wafer, 'W',
                BPItems.stone_tile);
        // GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.plate_assembly, 1), " # ", "SRS", "#C#", '#', BPItems.stone_tile, 'S',
        // "stickWood", 'R', BPItems.red_alloy_ingot, 'C', BPItems.stone_cathode));
        GameRegistry.addShapelessRecipe(new ItemStack(BPItems.tainted_silicon_chip_tile, 1), BPItems.silicon_chip_tile,
                Items.glowstone_dust);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.quartz_resonator_tile, 3), " # ", "###", "WWW", '#', "gemQuartz",
                'W', BPItems.stone_tile));

        GameRegistry.addShapelessRecipe(new ItemStack(BPItems.infused_teslatite_dust, 1), BPItems.teslatite_dust, Items.redstone);
        // TODO: stone_redwire recipe and stone_bundle recipe

        {
            for (RedwireType t : RedwireType.values()) {
                int i = 0;
                for (MinecraftColor c : MinecraftColor.WIRE_COLORS) {
                    ItemStack stack = PartManager.getPartInfo(
                            "wire." + t.getName() + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase())).getStack(12);
                    ItemStack freestanding = PartManager.getPartInfo(
                            "wire.freestanding." + t.getName() + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase()))
                            .getStack(1);

                    if (c == MinecraftColor.NONE) {
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "iii", 'i', t.getIngotOredictName()));
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "i", "i", "i", 'i', t.getIngotOredictName()));
                    } else {
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "www", "iii", "www", 'i', t.getIngotOredictName(), 'w',
                                new ItemStack(Blocks.wool, 1, i)));
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "wiw", "wiw", "wiw", 'i', t.getIngotOredictName(), 'w',
                                new ItemStack(Blocks.wool, 1, i)));
                    }
                    GameRegistry.addRecipe(new ShapedOreRecipe(freestanding, " s ", "sws", " s ", 's', "stickWood", 'w', stack.copy()));
                    i++;
                }
            }
        }
        // Bundled wire

        for (RedwireType t : RedwireType.values()) {
            ItemStack bundled = PartManager.getPartInfo("wire." + t.getName() + ".bundled").getStack(1);
            ItemStack freestanding = PartManager.getPartInfo("wire.freestanding." + t.getName() + ".bundled").getStack(1);

            GameRegistry.addRecipe(new ShapedOreRecipe(bundled, "sws", "www", "sws", 'w', t.getName() + "Insulated", 's', Items.string));
            GameRegistry.addRecipe(new ShapedOreRecipe(freestanding, " s ", "sws", " s ", 's', "stickWood", 'w', bundled.copy()));

            for (MinecraftColor c : MinecraftColor.VALID_COLORS) {
                ItemStack stack = PartManager.getPartInfo(
                        "wire." + t.getName() + ".bundled" + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase())).getStack(8);
                GameRegistry.addRecipe(new ShapedOreRecipe(stack, "www", "wdw", "www", 'w', bundled, 'd', "dye"
                        + StringUtils.capitalize(ItemDye.field_150923_a[15 - c.ordinal()])));
            }
        }

        // Blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_table), "WIW", "WCW", "WPW", 'W', "plankWood", 'I',
                PartManager.getPartInfo("integratedCircuit3x3").getStack(), 'C', Blocks.chest, 'P', BPBlocks.project_table));

        // Gates and Circuits
        GameRegistry.addRecipe(PartManager.getPartInfo("and").getStack(), "ACA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("buffer").getStack(), "ACA", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        // craftManager.addRecipe(PartManager.getPartInfo("bustransceiver"), "BBB", "N#N", "BBB", '#', BPItems.stone_tile, 'B',
        // BPItems.stone_bundle, 'N', BPItems.red_doped_wafer);
        GameRegistry.addRecipe(PartManager.getPartInfo("counter").getStack(), "#W#", "CPC", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile, 'P', BPItems.bluestone_pointer_tile);
        // craftManager.addRecipe(PartManager.getPartInfo("invert"), "#R#", "ROR", "#R#", '#', BPItems.stone_tile, 'O',
        // BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("lightCell").getStack(), "#W#", "#B#", "###", '#', BPItems.stone_tile, 'B',
                BPItems.blue_doped_wafer, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("multiplexer").getStack(), "ACA", "C#C", "ACW", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("nand").getStack(), "AAA", "CCC", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        // craftManager.addRecipe(PartManager.getPartInfo("noninvert"), "#R#", "ROR", "#RC", '#', BPItems.stone_tile, 'C',
        // BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("nor").getStack(), "#A#", "WCW", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("not").getStack(), "#A#", "ACA", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        // craftManager.addRecipe(PartManager.getPartInfo("null"), "#R#", "RRR", "#R#", '#', BPItems.stone_tile, 'R',
        // BPItems.stone_redwire);
        GameRegistry.addRecipe(PartManager.getPartInfo("or").getStack(), "#C#", "WCW", "#W#", '#', BPItems.stone_tile, 'C',
                BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("randomizer").getStack(), "#T#", "WWW", "TWT", '#', BPItems.stone_tile, 'T',
                BPItems.tainted_silicon_chip_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("pulseformer").getStack(), "ACA", "CAC", "WW#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("repeater").getStack(), "#CW", "#AW", "#WC", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("rs").getStack(), "WWA", "C#C", "AWW", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("sequencer").getStack(), "#C#", "CPC", "#C#", '#', BPItems.stone_tile, 'C',
                BPItems.bluestone_cathode_tile, 'P', BPItems.bluestone_pointer_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("state").getStack(), "#AC", "WXP", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'P', BPItems.bluestone_pointer_tile, 'W',
                BPItems.bluestone_wire_tile, 'X', BPItems.silicon_chip_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("synchronizer").getStack(), "WCW", "XAX", "WWW", 'A', BPItems.bluestone_anode_tile,
                'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile, 'X', BPItems.silicon_chip_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("timer").getStack(), "#W#", "WPW", "ACA", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile, 'P',
                BPItems.bluestone_pointer_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("toggle").getStack(), "C##", "WLW", "C##", '#', BPItems.stone_tile, 'C',
                BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile, 'L', Blocks.lever);
        GameRegistry.addRecipe(PartManager.getPartInfo("transparent").getStack(), "ACW", "CCC", "CW#", '#', BPItems.stone_tile, 'A',
                BPItems.bluestone_anode_tile, 'C', BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("xnor").getStack(), "ACA", "CAC", "WCW", 'A', BPItems.bluestone_anode_tile, 'C',
                BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("xor").getStack(), "AWA", "CAC", "WCW", 'A', BPItems.bluestone_anode_tile, 'C',
                BPItems.bluestone_cathode_tile, 'W', BPItems.bluestone_wire_tile);

        GameRegistry.addRecipe(PartManager.getPartInfo("comparator").getStack(), "TCT", "WRW", "CAC", 'A', BPItems.redstone_anode_tile,
                'C', BPItems.redstone_cathode_tile, 'W', BPItems.redstone_wire_tile, 'T', BPItems.stone_tile, 'R',
                BPItems.quartz_resonator_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("inverter").getStack(), "#A#", "ACA", "#W#", '#', BPItems.stone_tile, 'A',
                BPItems.redstone_anode_tile, 'C', BPItems.redstone_cathode_tile, 'W', BPItems.redstone_wire_tile);

        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit3x3").getStack(), "TST", "SSS", "TST", 'T', BPItems.stone_tile,
                'S', BPItems.silicon_chip_tile);
        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit5x5").getStack(), "II", "II", 'I',
                PartManager.getPartInfo("integratedCircuit3x3").getStack());
        GameRegistry.addRecipe(PartManager.getPartInfo("integratedCircuit7x7").getStack(), "II", "II", 'I',
                PartManager.getPartInfo("integratedCircuit5x5").getStack());
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_database, 1), "#C#", "BTB", "###", '#', "ingotIron", 'C',
                PartManager.getPartInfo("integratedCircuit7x7").getStack(), 'B', Blocks.bookshelf, 'T', BPBlocks.circuit_table));

        // Custom crafting for nullcells
        GameRegistry.addRecipe(RecipeNullCell.instance);
    }
}
