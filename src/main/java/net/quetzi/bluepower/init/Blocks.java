package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.quetzi.bluepower.blocks.BlockBasalt;
import net.quetzi.bluepower.blocks.BlockBasaltCobble;
import net.quetzi.bluepower.blocks.BlockMarble;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks {
    public static void init() {
        Block blockBasalt = new BlockBasalt();
        GameRegistry.registerBlock(blockBasalt, Refs.BLOCKBASALT_NAME);
        
        Block blockMarble = new BlockMarble();
        GameRegistry.registerBlock(blockMarble, Refs.BLOCKMARBLE_NAME);
        
        Block blockBasaltCobble = new BlockBasaltCobble();
        GameRegistry.registerBlock(blockBasaltCobble, Refs.BLOCKBASALTCOBBLE_NAME);
    }
}
