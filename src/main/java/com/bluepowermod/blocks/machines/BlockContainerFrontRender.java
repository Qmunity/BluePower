package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;

public class BlockContainerFrontRender extends BlockContainerBase {
    
    public BlockContainerFrontRender(Material material, Class<? extends TileEntity> tileEntityClass) {
    
        super(material, tileEntityClass);
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
