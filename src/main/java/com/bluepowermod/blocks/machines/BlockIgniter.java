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

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.tileentities.tier1.TileIgniter;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockIgniter extends BlockContainerBase {
    
    public BlockIgniter() {
    
        super(Material.rock, TileIgniter.class);
        setBlockName(Refs.BLOCKIGNITER_NAME);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        super.registerBlockIcons(iconRegister);
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKIGNITER_NAME + "_side_0");
    }
    
    @Override
    protected String getIconName(EnumFaceType faceType, boolean ejecting, boolean powered) {
    
        String iconName = textureName + "_" + faceType.toString().toLowerCase();
        if (faceType == EnumFaceType.FRONT) {
            if (ejecting) iconName += "_active";
        }
        return iconName;
    }
    
    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
    
        TileIgniter tile = (TileIgniter) world.getTileEntity(x, y, z);
        boolean orientation = tile.getFacingDirection() == ForgeDirection.UP;
        return orientation && tile.getIsRedstonePowered();
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
    
        return true;
    }
}
