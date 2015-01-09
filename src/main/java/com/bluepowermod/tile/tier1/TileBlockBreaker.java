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

package com.bluepowermod.tile.tier1;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import com.bluepowermod.tile.TileMachineBase;

public class TileBlockBreaker extends TileMachineBase {
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        
        if (!worldObj.isRemote && newValue && isBufferEmpty()) {
            ForgeDirection direction = getFacingDirection();
            Block breakBlock = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (!canBreakBlock(breakBlock, worldObj, xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)) return;
            int breakMeta = worldObj.getBlockMetadata(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            ArrayList<ItemStack> breakStacks = breakBlock.getDrops(worldObj, xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, breakMeta, 0);
            
            worldObj.func_147480_a(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, false); // destroyBlock
            addItemsToOutputBuffer(breakStacks);
        }
    }
    
    private boolean canBreakBlock(Block block, World world, int x, int y, int z) {
    
        return !world.isAirBlock(x, y, z) && !(block instanceof BlockLiquid) && !(block instanceof IFluidBlock) && block.getBlockHardness(world, x, y, z) > -1.0F;
    }
    
    @Override
    public boolean canConnectRedstone() {
    
        return true;
    }
}
