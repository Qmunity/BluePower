package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier1.TileDeployer;

/**
 * 
 * @author TheFjong
 *
 */
public class BlockDeployer extends BlockContainerBase {
    
    public BlockDeployer() {
    
        super(Material.rock);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
        setBlockName(Refs.BLOCKDEPLOYER_NAME);
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileDeployer.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.DEPLOYER_ID;
    }
    
    @Override
    protected String getIconName(EnumFaceType faceType, boolean ejecting, boolean powered) {
    
        String iconName = textureName + "_" + faceType.toString().toLowerCase();
        if (faceType == EnumFaceType.FRONT) {
            if (ejecting) iconName += "_active";
        }
        return iconName;
    }
}
