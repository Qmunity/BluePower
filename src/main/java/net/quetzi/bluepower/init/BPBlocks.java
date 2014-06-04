package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.quetzi.bluepower.blocks.BlockCrop;
import net.quetzi.bluepower.blocks.BlockCustomFlower;
import net.quetzi.bluepower.blocks.BlockItemOre;
import net.quetzi.bluepower.blocks.BlockStoneOre;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class BPBlocks {
	public static Block basalt;
    public static Block marble;
    public static Block basalt_cobble;
    public static Block basalt_brick;
    public static Block marble_brick;

    public static Block nikolite_ore;
    public static Block ruby_ore;
    public static Block sapphire_ore;
    public static Block malachite_ore;
    
    public static Block copper_ore;
    public static Block silver_ore;
    public static Block tin_ore;

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block malachite_block;
    public static Block nikolite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block tin_block;
    
    public static Block flax_crop;
    public static Block indigo_flower;

	
    public static void init() {
    	basalt = new BlockStoneOre(Refs.BASALT_NAME);
    	marble = new BlockStoneOre(Refs.MARBLE_NAME);
    	basalt_cobble  = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
    	basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
    	marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);

        nikolite_ore = new BlockItemOre(Refs.NIKOLITEORE_NAME, BPItems.nikolite);
        ruby_ore = new BlockItemOre(Refs.RUBYORE_NAME, BPItems.ruby);
        sapphire_ore = new BlockItemOre(Refs.SAPPHIREORE_NAME, BPItems.sapphire);
        malachite_ore = new BlockItemOre(Refs.MALACHITEORE_NAME, BPItems.malachite);
        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
        tin_ore = new BlockStoneOre(Refs.TINORE_NAME);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
        malachite_block = new BlockStoneOre(Refs.MALACHITEBLOCK_NAME);
        nikolite_block = new BlockStoneOre(Refs.NIKOLITEBLOCK_NAME);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
        tin_block = new BlockStoneOre(Refs.TINBLOCK_NAME);
        
        flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);
    	
        registerBlocks();
    }

    
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
        GameRegistry.registerBlock(indigo_flower, Refs.INDIGOFLOWER_NAME);
        
        //Todo: Add oreDictionary here. oreNikolite, oreTin etc
        //OreDictionary.registerOre(name, ore);

    }
}
