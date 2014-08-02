package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier3.TileKinectGenerator;

/**
 * 
 * @author TheFjong
 * 
 */
public class BlockKineticGenerator extends BlockContainerBase {
    
    public BlockKineticGenerator() {
    
        super(Material.iron, TileKinectGenerator.class);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
        setBlockName(Refs.KINETICGENERATOR_NAME);
        setBlockTextureName(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.KINETICGENERATOR_NAME + "_front");
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.KINETICGENERATOR_ID;
    }
    
}
