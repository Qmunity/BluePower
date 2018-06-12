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

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.reference.BPOredictNames;

public class OreDictionarySetup {

    public static void init() {

        // Ores
        OreDictionary.registerOre(BPOredictNames.ORE_COPPER, BPBlocks.copper_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_ZINC, BPBlocks.zinc_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_SILVER, BPBlocks.silver_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_TUNGSTEN, BPBlocks.tungsten_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_TESLATITE, BPBlocks.teslatite_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_AMETHYST, BPBlocks.amethyst_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_RUBY, BPBlocks.ruby_ore);
        OreDictionary.registerOre(BPOredictNames.ORE_SAPPHIRE, BPBlocks.sapphire_ore);

        // Storage blocks
        OreDictionary.registerOre(BPOredictNames.BLOCK_COPPER, BPBlocks.copper_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_ZINC, BPBlocks.zinc_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_SILVER, BPBlocks.silver_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_TUNGSTEN, BPBlocks.tungsten_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_TESLATITE, BPBlocks.teslatite_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_AMETHYST, BPBlocks.amethyst_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_RUBY, BPBlocks.ruby_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_SAPPHIRE, BPBlocks.sapphire_block);
        OreDictionary.registerOre(BPOredictNames.BLOCK_MALACHITE, BPBlocks.malachite_block);

        // Ingots
        OreDictionary.registerOre(BPOredictNames.INGOT_COPPER, BPItems.copper_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_ZINC, BPItems.zinc_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_SILVER, BPItems.silver_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_TUNGSTEN, BPItems.tungsten_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_TUNGCARB, BPItems.tungsten_carbide);

        OreDictionary.registerOre(BPOredictNames.INGOT_BLUE_ALLOY, BPItems.blue_alloy_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_RED_ALLOY, BPItems.red_alloy_ingot);
        OreDictionary.registerOre(BPOredictNames.INGOT_PURPLE_ALLOY, BPItems.purple_alloy_ingot);

        OreDictionary.registerOre(BPOredictNames.INGOT_BRASS, BPItems.brass_ingot);

        // Dusts
        OreDictionary.registerOre(BPOredictNames.DUST_TESLATITE, BPItems.teslatite_dust);
        OreDictionary.registerOre(BPOredictNames.DUST_INFUSED_TESLATITE, BPItems.infused_teslatite_dust);
        OreDictionary.registerOre(BPOredictNames.DUST_ZINC, BPItems.zinc_dust);

        // Nuggets
        OreDictionary.registerOre("nuggetTungsten", BPItems.tungsten_nugget);

        // Gems
        OreDictionary.registerOre(BPOredictNames.GEM_AMETHYST, BPItems.amethyst_gem);
        OreDictionary.registerOre(BPOredictNames.GEM_RUBY, BPItems.ruby_gem);
        OreDictionary.registerOre(BPOredictNames.GEM_SAPPHIRE, BPItems.sapphire_gem);

        // Dyes
        OreDictionary.registerOre(BPOredictNames.DYE_INDIGO, BPItems.indigo_dye);

        // Circuitry
        OreDictionary.registerOre(BPOredictNames.STONE_TILE, BPItems.stone_tile);
        OreDictionary.registerOre(BPOredictNames.BLUESTONE_TILE, BPItems.bluestone_wire_tile);
        OreDictionary.registerOre(BPOredictNames.BLUESTONE_ANODE, BPItems.bluestone_anode_tile);
        OreDictionary.registerOre(BPOredictNames.BLUESTONE_CATHODE, BPItems.bluestone_cathode_tile);
        OreDictionary.registerOre(BPOredictNames.BLUESTONE_POINTER, BPItems.bluestone_pointer_tile);
        OreDictionary.registerOre(BPOredictNames.REDSTONE_TILE, BPItems.redstone_wire_tile);
        OreDictionary.registerOre(BPOredictNames.REDSTONE_ANODE, BPItems.redstone_anode_tile);
        OreDictionary.registerOre(BPOredictNames.REDSTONE_CATHODE, BPItems.redstone_cathode_tile);
        OreDictionary.registerOre(BPOredictNames.REDSTONE_POINTER, BPItems.redstone_pointer_tile);
        OreDictionary.registerOre(BPOredictNames.SILICON_CHIP, BPItems.silicon_chip_tile);
        OreDictionary.registerOre(BPOredictNames.TAINTED_SILICON_CHIP, BPItems.tainted_silicon_chip_tile);
        OreDictionary.registerOre(BPOredictNames.QUARTZ_RESONATOR, BPItems.quartz_resonator_tile);
        OreDictionary.registerOre(BPOredictNames.BUNDLED_TILE, BPItems.stone_bundle);

        // Other
        OreDictionary.registerOre(BPOredictNames.MARBLE, BPBlocks.marble);
        OreDictionary.registerOre(BPOredictNames.BASALT, BPBlocks.basalt);

        for (Block b : BPBlocks.blockLamp)
            OreDictionary.registerOre("lampBP", b);
        for (Block b : BPBlocks.blockLampInverted)
            OreDictionary.registerOre("lampInvertedBP", b);

        //TODO Ore Dictionary Parts
    }

}
