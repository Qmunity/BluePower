package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.blocks.BlockGemOre;
import net.quetzi.bluepower.blocks.BlockNikoliteBlock;
import net.quetzi.bluepower.blocks.BlockNikoliteOre;
import net.quetzi.bluepower.blocks.BlockStoneOre;
import net.quetzi.bluepower.blocks.ContainerAlloyFurnace;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks {
    public static void init() {
        registerBlocks();
    }
    
    public static final Block basalt = (Block)Block.blockRegistry.getObject(Refs.BASALT_NAME);
    public static final Block marble = (Block)Block.blockRegistry.getObject(Refs.MARBLE_NAME);
    public static final Block basalt_cobble = (Block)Block.blockRegistry.getObject(Refs.BASALTCOBBLE_NAME);
    public static final Block basalt_brick = (Block)Block.blockRegistry.getObject(Refs.BASALTBRICK_NAME);
    public static final Block marble_brick = (Block)Block.blockRegistry.getObject(Refs.MARBLEBRICK_NAME);
    
    public static final Block nikolite_ore = (Block)Block.blockRegistry.getObject(Refs.NIKOLITEORE_NAME);
    public static final Block ruby_ore = (Block)Block.blockRegistry.getObject(Refs.RUBYORE_NAME);
    public static final Block sapphire_ore = (Block)Block.blockRegistry.getObject(Refs.SAPPHIREORE_NAME);
    public static final Block malachite_ore = (Block)Block.blockRegistry.getObject(Refs.MALACHITEORE_NAME);
    public static final Block copper_ore = (Block)Block.blockRegistry.getObject(Refs.COPPERORE_NAME);
    public static final Block silver_ore = (Block)Block.blockRegistry.getObject(Refs.SILVERORE_NAME);
    public static final Block tin_ore = (Block)Block.blockRegistry.getObject(Refs.TINORE_NAME);
    
    public static final Block ruby_block = (Block)Block.blockRegistry.getObject(Refs.RUBYBLOCK_NAME);
    public static final Block sapphire_block = (Block)Block.blockRegistry.getObject(Refs.SAPPHIREBLOCK_NAME);
    public static final Block malachite_block = (Block)Block.blockRegistry.getObject(Refs.MALACHITEBLOCK_NAME);
    public static final Block nikolite_block = (Block)Block.blockRegistry.getObject(Refs.NIKOLITEBLOCK_NAME);
    
    private static void registerBlocks() {
        GameRegistry.registerBlock(new BlockStoneOre(Refs.BASALT_NAME), Refs.BASALT_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.MARBLE_NAME), Refs.MARBLE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.BASALTCOBBLE_NAME), Refs.BASALTCOBBLE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.BASALTBRICK_NAME), Refs.BASALTBRICK_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.MARBLEBRICK_NAME), Refs.MARBLEBRICK_NAME);
        GameRegistry.registerBlock(new ContainerAlloyFurnace().setBlockName(Refs.ALLOYFURNACE_NAME), Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(new BlockNikoliteOre(Refs.NIKOLITEORE_NAME), Refs.NIKOLITEORE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.COPPERORE_NAME), Refs.COPPERORE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.SILVERORE_NAME), Refs.SILVERORE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.TINORE_NAME), Refs.TINORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.RUBYORE_NAME), Refs.RUBYORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.SAPPHIREORE_NAME), Refs.SAPPHIREORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.MALACHITEORE_NAME), Refs.MALACHITEORE_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.RUBYBLOCK_NAME), Refs.RUBYBLOCK_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME), Refs.SAPPHIREBLOCK_NAME);
        GameRegistry.registerBlock(new BlockStoneOre(Refs.MALACHITEBLOCK_NAME), Refs.MALACHITEBLOCK_NAME);
        GameRegistry.registerBlock(new BlockNikoliteBlock(Refs.NIKOLITEBLOCK_NAME), Refs.NIKOLITEBLOCK_NAME);
        
        OreDictionary.registerOre("oreCopper", Blocks.copper_ore);
        OreDictionary.registerOre("oreTin", Blocks.tin_ore);
        OreDictionary.registerOre("oreSilver", Blocks.silver_ore);
        OreDictionary.registerOre("oreRuby", Blocks.ruby_ore);
        OreDictionary.registerOre("oreMalachite", Blocks.malachite_ore);
        OreDictionary.registerOre("oreSapphire", Blocks.sapphire_ore);
        OreDictionary.registerOre("oreNikolite", Blocks.nikolite_ore);
    }
}
