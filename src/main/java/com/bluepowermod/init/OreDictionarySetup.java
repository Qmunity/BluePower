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

import net.minecraft.item.ItemDye;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.part.PartRegistry;

public class OreDictionarySetup {

    public static void init() {

        OreDictionary.registerOre("oreCopper", BPBlocks.copper_ore);
        OreDictionary.registerOre("oreZinc", BPBlocks.zinc_ore);
        OreDictionary.registerOre("oreSilver", BPBlocks.silver_ore);
        OreDictionary.registerOre("oreTungsten", BPBlocks.tungsten_ore);
        OreDictionary.registerOre("oreTeslatite", BPBlocks.teslatite_ore);
        OreDictionary.registerOre("oreAmethyst", BPBlocks.amethyst_ore);
        OreDictionary.registerOre("oreRuby", BPBlocks.ruby_ore);
        OreDictionary.registerOre("oreSapphire", BPBlocks.sapphire_ore);
        OreDictionary.registerOre("marble", BPBlocks.marble);
        OreDictionary.registerOre("basalt", BPBlocks.basalt);
        OreDictionary.registerOre("blockCopper", BPBlocks.copper_block);
        OreDictionary.registerOre("blockZinc", BPBlocks.zinc_block);
        OreDictionary.registerOre("blockSilver", BPBlocks.silver_block);
        OreDictionary.registerOre("blockTeslatite", BPBlocks.teslatite_block);
        OreDictionary.registerOre("blockAmethyst", BPBlocks.amethyst_block);
        OreDictionary.registerOre("blockRuby", BPBlocks.ruby_block);
        OreDictionary.registerOre("blockSapphire", BPBlocks.sapphire_block);

        OreDictionary.registerOre("gemRuby", BPItems.ruby_gem);
        OreDictionary.registerOre("gemAmethyst", BPItems.amethyst_gem);
        OreDictionary.registerOre("gemSapphire", BPItems.sapphire_gem);
        OreDictionary.registerOre("dustTeslatite", BPItems.teslatite_dust);
        OreDictionary.registerOre("dustInfusedteslatite", BPItems.infused_teslatite_dust);
        OreDictionary.registerOre("ingotCopper", BPItems.copper_ingot);
        OreDictionary.registerOre("ingotZinc", BPItems.zinc_ingot);
        OreDictionary.registerOre("ingotSilver", BPItems.silver_ingot);
        OreDictionary.registerOre("ingotBrass", BPItems.brass_ingot);
        OreDictionary.registerOre("ingotTungsten", BPItems.tungsten_ingot);
        OreDictionary.registerOre("nuggetTungsten", BPItems.tungsten_nugget);
        OreDictionary.registerOre("dyePurple", BPItems.indigo_dye);
        OreDictionary.registerOre("ingotBlueAlloy", BPItems.blue_alloy_ingot);
        OreDictionary.registerOre("waferStone", BPItems.stone_tile);
        OreDictionary.registerOre("ingotRedAlloy", BPItems.red_alloy_ingot);

        for (int i = 0; i < 16; i++) {
            OreDictionary.registerOre("bluestoneInsulated", PartRegistry.getInstance().getItemForPart("bluestoneWire." + ItemDye.field_150921_b[i]));
        }
    }

}
