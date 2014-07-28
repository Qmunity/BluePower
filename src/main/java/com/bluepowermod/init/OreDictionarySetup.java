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

import net.minecraftforge.oredict.OreDictionary;

public class OreDictionarySetup {

    public static void init() {

        OreDictionary.registerOre("oreCopper", BPBlocks.copper_ore);
        OreDictionary.registerOre("oreTin", BPBlocks.tin_ore);
        OreDictionary.registerOre("oreSilver", BPBlocks.silver_ore);
        OreDictionary.registerOre("oreTungsten", BPBlocks.tungsten_ore);

        OreDictionary.registerOre("gemRuby", BPItems.ruby);
        OreDictionary.registerOre("gemAmethyst", BPItems.amethyst);
        OreDictionary.registerOre("gemSapphire", BPItems.sapphire);
        OreDictionary.registerOre("dustNikolite", BPItems.nikolite);
        OreDictionary.registerOre("ingotCopper", BPItems.copper_ingot);
        OreDictionary.registerOre("ingotTin", BPItems.tin_ingot);
        OreDictionary.registerOre("ingotSilver", BPItems.silver_ingot);
        OreDictionary.registerOre("dyePurple", BPItems.indigo_dye);
    }
}
