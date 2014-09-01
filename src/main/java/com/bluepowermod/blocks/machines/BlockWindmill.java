/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.tileentities.tier2.TileWindmill;
import com.bluepowermod.util.Refs;

/**
 * 
 * @author TheFjong
 * 
 */
public class BlockWindmill extends Block implements ITileEntityProvider {
    
    public BlockWindmill() {
    
        super(Material.iron);
        setCreativeTab(CustomTabs.tabBluePowerItems);
        setBlockName(Refs.WINDMILL_NAME);
        setBlockBounds(0, 0, 0, 1, 1, 1);
    }
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
    
        return new TileWindmill();
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
    
        return false;
    }
    
    @Override
    public int getRenderType() {
    
        return -1;
    }
    
}
