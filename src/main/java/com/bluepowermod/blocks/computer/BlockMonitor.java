/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.blocks.computer;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.TileMonitor;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMonitor extends BlockContainerBase {
    
    @SideOnly(Side.CLIENT)
    protected IIcon topTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon frontTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon sideTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon backTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon bottomTexture;
    
    public BlockMonitor() {
    
        super(Material.iron, TileMonitor.class);
        setBlockName(Refs.BLOCKMONITOR_NAME);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
    
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileMonitor) {
            //((TileCPU)tileEntity).updateEntity();
            //Logs.log(Level.INFO, "[BluePowerControl] CPU TE ticked");
        }
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.MONITOR;
    }
    
    @Override
    public int tickRate(World world) {
    
        return 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    
        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        if (side == dir.ordinal()) {
            return topTexture;
        } else if (side == dir.getOpposite().ordinal()) {
            return bottomTexture;
        } else if (side == dir.WEST.ordinal()) {
            return frontTexture;
        } else if (side == dir.EAST.ordinal()) { return backTexture; }
        return sideTexture;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    
        TileMonitor tile = (TileMonitor) world.getTileEntity(x, y, z);
        ForgeDirection dir = tile.getFacingDirection();
        
        if (dir.ordinal() == side) {
            return frontTexture;
        } else if (dir.getOpposite().ordinal() == side) {
            return backTexture;
        } else if (dir.UP.ordinal() == side) {
            return topTexture;
        } else if (dir.DOWN.ordinal() == side) {
            return bottomTexture;
        } else {
            return sideTexture;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "monitor_front");
        sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
        topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
        backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_back");
        bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
        //this.frontTexture = this.blockIcon;
    }
    
}
