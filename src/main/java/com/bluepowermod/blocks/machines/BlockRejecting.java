/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.tileentities.IRejectAnimator;
import com.qmunity.lib.tileentity.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class BlockRejecting extends BlockContainerBase {

    public BlockRejecting(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material, tileEntityClass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        super.registerBlockIcons(iconRegister);

        boolean ejecting = false;
        boolean powered = false;

        do {
            do {
                String iconName = getIconName(EnumFaceType.SIDE, ejecting, powered) + "_rejecting";
                if (!textures.containsKey(iconName)) {
                    textures.put(iconName, iconRegister.registerIcon(iconName));
                }

                powered = !powered;
            } while (powered == true);
            ejecting = !ejecting;
        } while (ejecting == true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {

        boolean isRejecting = ((IRejectAnimator) te).isRejecting();
        if (faceType == EnumFaceType.SIDE && isRejecting) {
            String iconName = getIconName(faceType, ejecting, powered);
            return textures.get(iconName + "_rejecting");
        } else {
            return super.getIcon(faceType, ejecting, powered, side, te);
        }
    }
}
