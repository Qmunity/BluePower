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
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.supported.GateNullCell;
import com.bluepowermod.reference.BPOredictNames;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Quetzi on 27/10/14.
 */
public class LogicRecipes {

    public static void init() {

        // Components
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_wire_tile, 1), "#", "W", '#', "dustTeslatite", 'W',
                BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_anode_tile, 3), " # ", "###", "WWW", '#', "dustTeslatite", 'W',
                BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_cathode_tile, 1), "T#T", " W ", 'T', "dustTeslatite", '#',
                Blocks.REDSTONE_TORCH, 'W', BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_pointer_tile, 1), " S ", "T#T", " W ", 'S', "stone", '#',
                Blocks.REDSTONE_TORCH, 'W', BPOredictNames.STONE_TILE, 'T', "dustTeslatite"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.bluestone_pointer_tile, 1), "S", "T", 'S', "stone", 'T',
                BPOredictNames.BLUESTONE_CATHODE));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_wire_tile, 1), "#", "W", '#', "dustRedstone", 'W',
                BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_anode_tile, 3), " # ", "###", "WWW", '#', "dustRedstone", 'W',
                BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_cathode_tile, 1), "#", "W", '#', Blocks.REDSTONE_TORCH, 'W',
                BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_pointer_tile, 1), "S", "#", "W", 'S', "stone", '#',
                Blocks.REDSTONE_TORCH, 'W', BPOredictNames.STONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.redstone_pointer_tile, 1), "S", "T", 'S', "stone", 'T',
                BPOredictNames.REDSTONE_CATHODE));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.silicon_chip_tile, 1), " # ", "WWW", '#', BPItems.blue_doped_wafer, 'W',
                BPOredictNames.STONE_TILE));
        // GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.plate_assembly, 1), " # ", "SRS", "#C#", '#', BPOredictNames.STONE_TILE,
        // 'S',
        // "stickWood", 'R', BPItems.red_alloy_ingot, 'C', BPItems.stone_cathode)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.tainted_silicon_chip_tile, 1), BPOredictNames.SILICON_CHIP,
                Items.GLOWSTONE_DUST));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.quartz_resonator_tile, 3), " # ", "###", "WWW", '#', "gemQuartz", 'W',
                BPOredictNames.STONE_TILE));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPItems.stone_bundle, 1), "#", "W", '#', "wireBundled", 'W',
                BPOredictNames.STONE_TILE));

        GameRegistry
                .addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.infused_teslatite_dust, 1), BPOredictNames.DUST_TESLATITE, Items.REDSTONE));

        {
            for (RedwireType t : RedwireType.values()) {
                int i = 0;
                for (MinecraftColor c : MinecraftColor.WIRE_COLORS) {
                    ItemStack stack = PartManager.getPartInfo("wire." + t.getName() + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase()))
                            .getStack(12);
                    ItemStack freestanding = PartManager.getPartInfo(
                            "wire.freestanding." + t.getName() + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase())).getStack(1);

                    if (c == MinecraftColor.NONE) {
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "iii", 'i', t.getIngotOredictName()));
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "i", "i", "i", 'i', t.getIngotOredictName()));
                    } else {
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "www", "iii", "www", 'i', t.getIngotOredictName(), 'w',
                                new ItemStack(Blocks.WOOL, 1, i)));
                        GameRegistry.addRecipe(new ShapedOreRecipe(stack.copy(), "wiw", "wiw", "wiw", 'i', t.getIngotOredictName(), 'w',
                                new ItemStack(Blocks.WOOL, 1, i)));
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

            GameRegistry.addRecipe(new ShapedOreRecipe(bundled, "sws", "www", "sws", 'w', t.getName() + "Insulated", 's', Items.STRING));
            GameRegistry.addRecipe(new ShapedOreRecipe(freestanding, " s ", "sws", " s ", 's', "stickWood", 'w', bundled.copy()));

            for (MinecraftColor c : MinecraftColor.VALID_COLORS) {
                ItemStack stack = PartManager.getPartInfo("wire." + t.getName() + ".bundled" + (c == MinecraftColor.NONE ? "" : "." + c.name().toLowerCase())).getStack(8);
                //ToDo Change this back to OreDict
                GameRegistry.addRecipe(new ShapedOreRecipe(stack, "www", "wdw", "www", 'w', bundled, 'd', "dye" + c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase()));
            }
        }

        // Blocks
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_table), "WIW", "WCW", "WPW", 'W', "plankWood", 'I', PartManager
                .getPartInfo("integratedCircuit3x3").getStack(), 'C', Blocks.CHEST, 'P', BPBlocks.project_table));

        // Gates and Circuits
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("and").getStack(), "ACA", "CCC", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("buffer").getStack(), "ACA", "WCW", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        // craftManager.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("bustransceiver"), "BBB", "N#N", "BBB", '#', BPOredictNames.STONE_TILE,
        // 'B',
        // BPItems.stone_bundle, 'N', BPItems.red_doped_wafer));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("counter").getStack(), "#W#", "CPC", "#W#", '#',
                BPOredictNames.STONE_TILE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE, 'P',
                BPOredictNames.BLUESTONE_POINTER));
        // craftManager.addRecipe(PartManager.getPartInfo("invert"), "#R#", "ROR", "#R#", '#', BPOredictNames.STONE_TILE, 'O',
        // BPItems.plate_assembly, 'R', BPItems.stone_redwire));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("lightCell").getStack(), "#W#", "#B#", "###", '#',
                BPOredictNames.STONE_TILE, 'B', BPItems.blue_doped_wafer, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("multiplexer").getStack(), "ACA", "C#C", "ACW", '#',
                BPOredictNames.STONE_TILE, 'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W',
                BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("nand").getStack(), "AAA", "CCC", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        // craftManager.addRecipe(PartManager.getPartInfo("noninvert"), "#R#", "ROR", "#RC", '#', BPOredictNames.STONE_TILE, 'C',
        // BPItems.stone_cathode, 'O', BPItems.plate_assembly, 'R', BPItems.stone_redwire));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("nor").getStack(), "#A#", "WCW", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("not").getStack(), "#A#", "ACA", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        // craftManager.addRecipe(PartManager.getPartInfo("null"), "#R#", "RRR", "#R#", '#', BPOredictNames.STONE_TILE, 'R',
        // BPItems.stone_redwire));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("or").getStack(), "#C#", "WCW", "#W#", '#', BPOredictNames.STONE_TILE,
                'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("randomizer").getStack(), "#T#", "WWW", "TWT", '#',
                BPOredictNames.STONE_TILE, 'T', BPOredictNames.TAINTED_SILICON_CHIP, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("pulseformer").getStack(), "ACA", "CAC", "WW#", '#',
                BPOredictNames.STONE_TILE, 'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W',
                BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("repeater").getStack(), "#CW", "#AW", "#WC", '#',
                BPOredictNames.STONE_TILE, 'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W',
                BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("rs").getStack(), "WWA", "C#C", "AWW", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("sequencer").getStack(), "#C#", "CPC", "#C#", '#',
                BPOredictNames.STONE_TILE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'P', BPOredictNames.BLUESTONE_POINTER));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("state").getStack(), "#AC", "WXP", "#W#", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'P', BPOredictNames.BLUESTONE_POINTER, 'W',
                BPOredictNames.BLUESTONE_TILE, 'X', BPOredictNames.SILICON_CHIP));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("synchronizer").getStack(), "WCW", "XAX", "WWW", 'A',
                BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE, 'X',
                BPOredictNames.SILICON_CHIP));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("timer").getStack(), "#W#", "WPW", "ACA", '#', BPOredictNames.STONE_TILE,
                'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE, 'P',
                BPOredictNames.BLUESTONE_POINTER));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("toggle").getStack(), "C##", "WLW", "C##", '#', BPOredictNames.STONE_TILE,
                'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE, 'L', Blocks.LEVER));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("transparent").getStack(), "ACW", "CCC", "CW#", '#',
                BPOredictNames.STONE_TILE, 'A', BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W',
                BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("xnor").getStack(), "ACA", "CAC", "WCW", 'A',
                BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("xor").getStack(), "AWA", "CAC", "WCW", 'A',
                BPOredictNames.BLUESTONE_ANODE, 'C', BPOredictNames.BLUESTONE_CATHODE, 'W', BPOredictNames.BLUESTONE_TILE));

        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("comparator").getStack(), "TCT", "WRW", "CAC", 'A',
                BPOredictNames.REDSTONE_ANODE, 'C', BPOredictNames.REDSTONE_CATHODE, 'W', BPOredictNames.REDSTONE_TILE, 'T',
                BPOredictNames.STONE_TILE, 'R', BPOredictNames.QUARTZ_RESONATOR));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("inverter").getStack(), "#A#", "ACA", "#W#", '#',
                BPOredictNames.STONE_TILE, 'A', BPOredictNames.REDSTONE_ANODE, 'C', BPOredictNames.REDSTONE_CATHODE, 'W',
                BPOredictNames.REDSTONE_TILE));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("regulabletorch").getStack(), "#W#", "#P#", "###", '#',
                BPOredictNames.STONE_TILE, 'P', BPOredictNames.REDSTONE_POINTER, 'W', BPOredictNames.REDSTONE_TILE));

        GameRegistry.addRecipe(new ShapedOreRecipe(GateNullCell.getStackWithData(new GateNullCell()), "SSS", "S S", "WWW", 'S', "stickWood", 'W',
                BPOredictNames.STONE_TILE));

        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("integratedCircuit3x3").getStack(), "TST", "SSS", "TST", 'T',
                BPOredictNames.STONE_TILE, 'S', BPOredictNames.SILICON_CHIP));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("integratedCircuit5x5").getStack(), "II", "II", 'I', PartManager
                .getPartInfo("integratedCircuit3x3").getStack()));
        GameRegistry.addRecipe(new ShapedOreRecipe(PartManager.getPartInfo("integratedCircuit7x7").getStack(), "II", "II", 'I', PartManager
                .getPartInfo("integratedCircuit5x5").getStack()));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BPBlocks.circuit_database, 1), "#C#", "BTB", "###", '#', "ingotIron", 'C',
                PartManager.getPartInfo("integratedCircuit7x7").getStack(), 'B', Blocks.BOOKSHELF, 'T', BPBlocks.circuit_table));

        // Custom crafting for nullcells
        GameRegistry.addRecipe(RecipeNullCell.instance);
    }
}
