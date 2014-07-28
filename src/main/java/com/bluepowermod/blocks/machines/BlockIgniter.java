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
 */

package com.bluepowermod.blocks.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier1.TileIgniter;

public class BlockIgniter extends BlockContainerBase {

    private IIcon textureFrontOn;
    private IIcon textureFrontOff;
    private IIcon textureSide1;
    private IIcon textureSide2;
    private IIcon textureBack;

    public BlockIgniter() {

        super(Material.rock);
        this.setBlockName(Refs.BLOCKIGNITER_NAME);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {

        return TileIgniter.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        // TODO: Set textures correctly
        ForgeDirection direction = ForgeDirection.getOrientation(meta);
        if (side == direction.ordinal()) {
            return textureFrontOff;
        } else if (side == direction.getOpposite().ordinal()) {
            return textureBack;
        } if ((side == 3) || (side == 2)) {
            return textureSide2;
        }
        return blockIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        this.textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_front_on");
        this.textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_front_off");
        this.textureBack = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_back");
        this.textureSide1 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_side_0");
        this.textureSide2 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_side_1");
        this.blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_side_0");
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.INVALID;
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {

        boolean orientation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)) == ForgeDirection.UP;
        TileIgniter tile = (TileIgniter) world.getTileEntity(x, y, z);
        return orientation && tile.getIsRedstonePowered();
    }
}
