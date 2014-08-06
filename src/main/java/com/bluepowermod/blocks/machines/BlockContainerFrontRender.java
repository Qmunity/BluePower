package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.tileentities.TileBase;

/**
 * @author MineMaarten
 */
public class BlockContainerFrontRender extends BlockContainerBase {

    public BlockContainerFrontRender(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material, tileEntityClass);
    }

    @Override
    protected String getIconName(EnumFaceType faceType, boolean ejecting, boolean powered) {

        String iconName = textureName + "_" + faceType.toString().toLowerCase();
        if (faceType == EnumFaceType.FRONT) {
            if (ejecting)
                iconName += "_active";
        }
        return iconName;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        TileBase tb = (TileBase) world.getTileEntity(x, y, z);
        if (tb == null)
            return false;

        return tb.canConnectRedstone();
    }
}
