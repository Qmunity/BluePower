package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.quetzi.bluepower.blocks.BlockBasalt;
import net.quetzi.bluepower.blocks.BlockBasaltBrick;
import net.quetzi.bluepower.blocks.BlockBasaltCobble;
import net.quetzi.bluepower.blocks.BlockGemOre;
import net.quetzi.bluepower.blocks.BlockMalachiteBlock;
import net.quetzi.bluepower.blocks.BlockMarble;
import net.quetzi.bluepower.blocks.BlockMarbleBrick;
import net.quetzi.bluepower.blocks.BlockNikoliteBlock;
import net.quetzi.bluepower.blocks.BlockNikoliteOre;
import net.quetzi.bluepower.blocks.BlockRubyBlock;
import net.quetzi.bluepower.blocks.BlockSapphireBlock;
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
    public static final Block ruby_block = (Block)Block.blockRegistry.getObject(Refs.RUBYBLOCK_NAME);
    public static final Block sapphire_block = (Block)Block.blockRegistry.getObject(Refs.SAPPHIREBLOCK_NAME);
    public static final Block malachite_block = (Block)Block.blockRegistry.getObject(Refs.MALACHITEBLOCK_NAME);
    public static final Block nikolite_block = (Block)Block.blockRegistry.getObject(Refs.NIKOLITEBLOCK_NAME);
    
    private static void registerBlocks() {
        GameRegistry.registerBlock(new BlockBasalt().setBlockName(Refs.BASALT_NAME), Refs.BASALT_NAME);
        GameRegistry.registerBlock(new BlockMarble().setBlockName(Refs.MARBLE_NAME), Refs.MARBLE_NAME);
        GameRegistry.registerBlock(new BlockBasaltCobble().setBlockName(Refs.BASALTCOBBLE_NAME), Refs.BASALTCOBBLE_NAME);
        GameRegistry.registerBlock(new BlockBasaltBrick().setBlockName(Refs.BASALTBRICK_NAME), Refs.BASALTBRICK_NAME);
        GameRegistry.registerBlock(new BlockMarbleBrick().setBlockName(Refs.MARBLEBRICK_NAME), Refs.MARBLEBRICK_NAME);
        GameRegistry.registerBlock(new ContainerAlloyFurnace().setBlockName(Refs.ALLOYFURNACE_NAME), Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(new BlockNikoliteOre().setBlockName(Refs.NIKOLITEORE_NAME), Refs.NIKOLITEORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.RUBYORE_NAME).setBlockName(Refs.RUBYORE_NAME), Refs.RUBYORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.SAPPHIREORE_NAME).setBlockName(Refs.SAPPHIREORE_NAME), Refs.SAPPHIREORE_NAME);
        GameRegistry.registerBlock(new BlockGemOre(Refs.MALACHITEORE_NAME).setBlockName(Refs.MALACHITEORE_NAME), Refs.MALACHITEORE_NAME);
        GameRegistry.registerBlock(new BlockRubyBlock().setBlockName(Refs.RUBYBLOCK_NAME), Refs.RUBYBLOCK_NAME);
        GameRegistry.registerBlock(new BlockSapphireBlock().setBlockName(Refs.SAPPHIREBLOCK_NAME), Refs.SAPPHIREBLOCK_NAME);
        GameRegistry.registerBlock(new BlockMalachiteBlock().setBlockName(Refs.MALACHITEBLOCK_NAME), Refs.MALACHITEBLOCK_NAME);
        GameRegistry.registerBlock(new BlockNikoliteBlock().setBlockName(Refs.NIKOLITEBLOCK_NAME), Refs.NIKOLITEBLOCK_NAME);    
    }
}
