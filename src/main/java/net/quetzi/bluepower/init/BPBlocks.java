package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.blocks.BlockCrop;
import net.quetzi.bluepower.blocks.BlockCustomFlower;
import net.quetzi.bluepower.blocks.BlockItemOre;
import net.quetzi.bluepower.blocks.BlockStoneOre;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class BPBlocks {
    public static void init() {
        registerBlocks();
    }

    public static final Block basalt = new BlockStoneOre(Refs.BASALT_NAME);
    public static final Block marble = new BlockStoneOre(Refs.MARBLE_NAME);
    public static final Block basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
    public static final Block basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
    public static final Block marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);

    public static final Block nikolite_ore = new BlockItemOre(Refs.NIKOLITEORE_NAME, BPItems.nikolite);
    public static final Block ruby_ore = new BlockItemOre(Refs.RUBYORE_NAME, BPItems.ruby);
    public static final Block sapphire_ore = new BlockItemOre(Refs.SAPPHIREORE_NAME, BPItems.sapphire);
    public static final Block malachite_ore = new BlockItemOre(Refs.MALACHITEORE_NAME,
            BPItems.malachite);
    public static final Block copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
    public static final Block silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
    public static final Block tin_ore = new BlockStoneOre(Refs.TINORE_NAME);

    public static final Block ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
    public static final Block sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
    public static final Block malachite_block = new BlockStoneOre(Refs.MALACHITEBLOCK_NAME);
    public static final Block nikolite_block = new BlockStoneOre(Refs.NIKOLITEBLOCK_NAME);
    public static final Block copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
    public static final Block silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
    public static final Block tin_block = new BlockStoneOre(Refs.TINBLOCK_NAME);
    
    public static final Block flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
    public static final Block indigo_flower = new BlockCustomFlower(0).setBlockName(Refs.INDIGOFLOWER_NAME);

    private static void registerBlocks() {
        GameRegistry.registerBlock(basalt, Refs.BASALT_NAME);
        GameRegistry.registerBlock(marble, Refs.MARBLE_NAME);
        GameRegistry.registerBlock(basalt_cobble, Refs.BASALTCOBBLE_NAME);
        GameRegistry.registerBlock(basalt_brick, Refs.BASALTBRICK_NAME);
        GameRegistry.registerBlock(marble_brick, Refs.MARBLEBRICK_NAME);

        GameRegistry.registerBlock(nikolite_ore, Refs.NIKOLITEORE_NAME);
        GameRegistry.registerBlock(copper_ore, Refs.COPPERORE_NAME);
        GameRegistry.registerBlock(silver_ore, Refs.SILVERORE_NAME);
        GameRegistry.registerBlock(tin_ore, Refs.TINORE_NAME);
        GameRegistry.registerBlock(ruby_ore, Refs.RUBYORE_NAME);
        GameRegistry.registerBlock(sapphire_ore, Refs.SAPPHIREORE_NAME);
        GameRegistry.registerBlock(malachite_ore, Refs.MALACHITEORE_NAME);
        GameRegistry.registerBlock(ruby_block, Refs.RUBYBLOCK_NAME);
        GameRegistry.registerBlock(sapphire_block, Refs.SAPPHIREBLOCK_NAME);
        GameRegistry.registerBlock(malachite_block, Refs.MALACHITEBLOCK_NAME);
        GameRegistry.registerBlock(nikolite_block, Refs.NIKOLITEBLOCK_NAME);
        GameRegistry.registerBlock(copper_block, Refs.COPPERBLOCK_NAME);
        GameRegistry.registerBlock(silver_block, Refs.SILVERBLOCK_NAME);
        GameRegistry.registerBlock(tin_block, Refs.TINBLOCK_NAME);
        
        GameRegistry.registerBlock(flax_crop, Refs.FLAXCROP_NAME);

        OreDictionary.registerOre("oreCopper", copper_ore);
        OreDictionary.registerOre("oreTin", tin_ore);
        OreDictionary.registerOre("oreSilver", silver_ore);
        OreDictionary.registerOre("gemRuby", ruby_ore);
        OreDictionary.registerOre("gemMalachite", malachite_ore);
        OreDictionary.registerOre("gemSapphire", sapphire_ore);
        OreDictionary.registerOre("dustNikolite", nikolite_ore);

    }
}
