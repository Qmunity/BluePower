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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier1.TileAlloyFurnace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyFurnace extends BlockContainerBase {
    
    public static IIcon textureTop;
    private IIcon       textureBottom;
    private IIcon       textureSide;
    private IIcon       textureFrontOn;
    private IIcon       textureFrontOff;
    
    public BlockAlloyFurnace() {
    
        super(Material.rock);
        setBlockName(Refs.ALLOYFURNACE_NAME);
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    
        TileAlloyFurnace te = (TileAlloyFurnace) world.getTileEntity(x, y, z);
        ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
        if (forgeSide == ForgeDirection.UP) return textureTop;
        if (forgeSide == ForgeDirection.DOWN) return textureBottom;
        if (forgeSide == te.getFacingDirection()) return te.getIsActive() ? textureFrontOn : textureFrontOff;
        return textureSide;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    
        ForgeDirection s = ForgeDirection.getOrientation(side);
        // If is facing
        
        if (3 == side) { return textureFrontOff; }
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {
    
        TileAlloyFurnace te = (TileAlloyFurnace) world.getTileEntity(x, y, z);
        if (te.getIsActive()) {
            int l = te.getFacingDirection().ordinal();
            float f = x + 0.5F;
            float f1 = y + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = z + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;
            
            if (l == 4) {
                world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
    
    // Not sure if you need this function.
    @Override
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
    
        return Item.getItemFromBlock(BPBlocks.alloy_furnace);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        textureTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYFURNACE_NAME + "_top");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYFURNACE_NAME + "_bottom");
        textureSide = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYFURNACE_NAME + "_side");
        textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYFURNACE_NAME + "_front_on");
        textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYFURNACE_NAME + "_front_off");
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
    
        TileAlloyFurnace te = (TileAlloyFurnace) world.getTileEntity(x, y, z);
        return te.getIsActive() ? 13 : 0;
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileAlloyFurnace.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.ALLOY_FURNACE;
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
