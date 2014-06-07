package net.quetzi.bluepower.compat.fmp;

import net.minecraft.block.Block;
import net.quetzi.bluepower.compat.CompatModule;
import net.quetzi.bluepower.init.BPBlocks;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleFMP extends CompatModule {
    
    @Override
    public void preInit(FMLPreInitializationEvent ev) {
    
    }
    
    @Override
    public void init(FMLInitializationEvent ev) {
    
        RegisterMultiparts.register();
        
        registerBlocksAsMicroblock();
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent ev) {
    
    }
    
    private void registerBlocksAsMicroblock() {
    
        registerBlockAsMicroblock(BPBlocks.basalt);
        registerBlockAsMicroblock(BPBlocks.basalt_brick);
        registerBlockAsMicroblock(BPBlocks.basalt_brick_small);
        registerBlockAsMicroblock(BPBlocks.basalt_cobble);
        registerBlockAsMicroblock(BPBlocks.basalt_tile);
        registerBlockAsMicroblock(BPBlocks.basalt_paver);
        
        registerBlockAsMicroblock(BPBlocks.fancy_basalt);
        registerBlockAsMicroblock(BPBlocks.fancy_marble);
        
        registerBlockAsMicroblock(BPBlocks.marble);
        registerBlockAsMicroblock(BPBlocks.marble_brick);
        registerBlockAsMicroblock(BPBlocks.marble_brick_small);
        registerBlockAsMicroblock(BPBlocks.marble_tile);
        registerBlockAsMicroblock(BPBlocks.marble_paver);
    }
    
    private void registerBlockAsMicroblock(Block b) {
    
        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(b, 0),
                b.getUnlocalizedName());
    }
    
}
