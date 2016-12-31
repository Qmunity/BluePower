/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.RendererBlockBase.EnumFaceType;
import com.bluepowermod.tile.IRejectAnimator;
import com.bluepowermod.tile.TileBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class BlockRejecting extends BlockContainerBase {

    public BlockRejecting(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material, tileEntityClass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap iconRegister) {

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
            } while (powered);
            ejecting = !ejecting;
        } while (ejecting);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {

        boolean isRejecting = ((IRejectAnimator) te).isRejecting();
        if (faceType == EnumFaceType.SIDE && isRejecting) {
            String iconName = getIconName(faceType, ejecting, powered);
            return textures.get(iconName + "_rejecting");
        } else {
            return super.getIcon(faceType, ejecting, powered, side, te);
        }
    }
}
