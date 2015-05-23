/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *
 *     @author Quetzi
 */

package com.bluepowermod.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.tile.IRotatable;
import uk.co.qmunity.lib.tile.TileBase;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.RendererBlockBase.EnumFaceType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class BlockContainerTwoSideRender extends BlockContainerBase {

    public BlockContainerTwoSideRender(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material, tileEntityClass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        super.registerBlockIcons(iconRegister);
        blockIcon = iconRegister.registerIcon(getTextureName() + "_side_0");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return side == 2 || side == 3 ? blockIcon : super.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {

        if (faceType == EnumFaceType.SIDE) {
            ForgeDirection orientation = ((IRotatable) te).getFacingDirection();
            if (orientation.ordinal() < 2) {
                if (side == ForgeDirection.WEST.ordinal() || side == ForgeDirection.EAST.ordinal())
                    return blockIcon;
            } else {
                if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
                    return blockIcon;
            }
        }
        return super.getIcon(faceType, ejecting, powered, side, te);
    }

}
