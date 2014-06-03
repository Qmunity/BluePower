package net.quetzi.bluepower.init;

import net.minecraftforge.oredict.OreDictionary;


public class OreDictionarySetup {
    public static void init() {
        OreDictionary.registerOre("oreCopper", BPBlocks.copper_ore);
        OreDictionary.registerOre("oreTin", BPBlocks.tin_ore);
        OreDictionary.registerOre("oreSilver", BPBlocks.silver_ore);
        
        OreDictionary.registerOre("gemRuby", BPItems.ruby);
        OreDictionary.registerOre("gemMalachite", BPItems.malachite);
        OreDictionary.registerOre("gemSapphire", BPItems.sapphire);
        OreDictionary.registerOre("dustNikolite", BPItems.nikolite);
        OreDictionary.registerOre("ingotCopper", BPItems.copper_ingot);
        OreDictionary.registerOre("ingotTin", BPItems.tin_ingot);
        OreDictionary.registerOre("ingotSilver", BPItems.silver_ingot);
        OreDictionary.registerOre("dyePurple", BPItems.indigo_dye);
    }
}
