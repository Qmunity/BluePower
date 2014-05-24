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

public class Blocks{
    public static void init(){
        registerBlocks();
    }

    public static final Block basalt = new BlockStoneOre(Refs.BASALT_NAME);
    public static final Block marble = new BlockStoneOre(Refs.MARBLE_NAME);
    public static final Block basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
    public static final Block basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
    public static final Block marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);

    public static final Block nikolite_ore = new BlockNikoliteOre(Refs.NIKOLITEORE_NAME);
    public static final Block ruby_ore = new BlockGemOre(Refs.RUBYORE_NAME);
    public static final Block sapphire_ore = new BlockGemOre(Refs.SAPPHIREORE_NAME);
    public static final Block malachite_ore = new BlockGemOre(Refs.MALACHITEORE_NAME);
    public static final Block copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
    public static final Block silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
    public static final Block tin_ore = new BlockStoneOre(Refs.TINORE_NAME);

    public static final Block ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
    public static final Block sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
    public static final Block malachite_block = new BlockStoneOre(Refs.MALACHITEBLOCK_NAME);
    public static final Block nikolite_block = new BlockNikoliteBlock(Refs.NIKOLITEBLOCK_NAME);

    private static void registerBlocks(){
        GameRegistry.registerBlock(basalt, Refs.BASALT_NAME);
        GameRegistry.registerBlock(marble, Refs.MARBLE_NAME);
        GameRegistry.registerBlock(basalt_cobble, Refs.BASALTCOBBLE_NAME);
        GameRegistry.registerBlock(basalt_brick, Refs.BASALTBRICK_NAME);
        GameRegistry.registerBlock(marble_brick, Refs.MARBLEBRICK_NAME);
        GameRegistry.registerBlock(new ContainerAlloyFurnace().setBlockName(Refs.ALLOYFURNACE_NAME), Refs.ALLOYFURNACE_NAME);
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

        OreDictionary.registerOre("oreCopper", Blocks.copper_ore);
        OreDictionary.registerOre("oreTin", Blocks.tin_ore);
        OreDictionary.registerOre("oreSilver", Blocks.silver_ore);
        OreDictionary.registerOre("oreRuby", Blocks.ruby_ore);
        OreDictionary.registerOre("oreMalachite", Blocks.malachite_ore);
        OreDictionary.registerOre("oreSapphire", Blocks.sapphire_ore);
        OreDictionary.registerOre("oreNikolite", Blocks.nikolite_ore);
    }
}
