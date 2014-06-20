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

package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileTransposer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTransposer extends BlockContainerBase {

    private IIcon textureFront;
    private IIcon textureSide;
    private IIcon textureSideActive;
    private IIcon textureBack;

    public BlockTransposer() {

        super(Material.rock);
        this.setBlockName(Refs.TRANSPOSER_NAME);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {

        return TileTransposer.class;
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.INVALID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection direction = ForgeDirection.getOrientation(meta);
        if (side == direction.ordinal()) {
            return textureFront;
        } else if (side == direction.getOpposite().ordinal()) { return textureBack; }
        // if () { return textureSideActive; } TODO: Check if block is powered
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        this.textureFront = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.TRANSPOSER_NAME + "_front");
        this.textureBack = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.TRANSPOSER_NAME + "_back");
        this.textureSide = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.TRANSPOSER_NAME + "_side_0");
        this.textureSideActive = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.TRANSPOSER_NAME + "_side_0_active");
        this.blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.TRANSPOSER_NAME + "_side_0");
    }
}
