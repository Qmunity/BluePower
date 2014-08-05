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

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier1.TileProjectTable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockProjectTable extends BlockContainerBase {
    
    public static IIcon textureTop;
    private IIcon       textureBottom;
    private IIcon       textureSide;
    private IIcon       textureFront;
    
    public BlockProjectTable() {
    
        super(Material.rock, TileProjectTable.class);
        setBlockName(Refs.PROJECTTABLE_NAME);
        // This might not be needed actually.
        setBlockTextureName(Refs.PROJECTTABLE_NAME + "_front");
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    
        ForgeDirection s = ForgeDirection.getOrientation(side);
        // If is facing
        
        if (meta == side) { return textureFront; }
        switch (s) {
            case UP:
                return textureTop;
            case DOWN:
                return textureBottom;
            case EAST:
            case NORTH:
            case SOUTH:
            case WEST:
            case UNKNOWN:
                return textureSide;
            default:
                break;
        
        }
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
    
        return true;
    }
    
    // Not sure if you need this function.
    @Override
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
    
        return Item.getItemFromBlock(BPBlocks.project_table);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        textureTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.PROJECTTABLE_NAME + "_top");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.PROJECTTABLE_NAME + "_bottom");
        textureSide = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.PROJECTTABLE_NAME + "_side");
        textureFront = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.PROJECTTABLE_NAME + "_front");
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
    
        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & 8) != 0) { return 13; }
        return 0;
    }
    
    @Override
    protected boolean canRotateVertical() {
    
        return false;
    }
    
    @Override
    public int getRenderType() {
    
        return 0;
    }
}
