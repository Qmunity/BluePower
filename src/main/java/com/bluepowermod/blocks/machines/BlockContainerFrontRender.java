/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.qmunity.lib.tileentity.TileBase;

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
