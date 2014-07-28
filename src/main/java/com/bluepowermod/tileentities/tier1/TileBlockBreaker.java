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

package com.bluepowermod.tileentities.tier1;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.TileMachineBase;

public class TileBlockBreaker extends TileMachineBase {
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        
        if (!worldObj.isRemote && newValue && isBufferEmpty()) {
            ForgeDirection direction = getFacingDirection();
            ForgeDirection oppDirection = direction.getOpposite();
            Block breakBlock = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            int breakMeta = worldObj.getBlockMetadata(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            float breakHardness = breakBlock.getBlockHardness(worldObj, xCoord, yCoord, zCoord);
            ArrayList<ItemStack> breakStacks = breakBlock.getDrops(worldObj, xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, breakMeta, 0);
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + oppDirection.offsetX, yCoord + oppDirection.offsetY, zCoord + oppDirection.offsetZ);
                        
            if(breakHardness == -1.0F) {
            	return;
            }
            
            worldObj.func_147480_a(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, false); //destroyBlock
            
            if (IOHelper.canInterfaceWith(tileEntity, direction)) {
                for (ItemStack breakStack : breakStacks) {
                    ItemStack returnedStack = IOHelper.insert(tileEntity, breakStack, direction, false);
                    if (returnedStack != null) {
                        this.addItemToOutputBuffer(returnedStack);
                    }
                }
                
            } else if (worldObj.isAirBlock(xCoord + oppDirection.offsetX, yCoord + oppDirection.offsetY, zCoord + oppDirection.offsetZ)) {
                for (ItemStack breakStack : breakStacks) {
                    ejectItemInWorld(breakStack, oppDirection);
                }
            } else {
                addItemsToOutputBuffer(breakStacks);
            }
        }
    }
}
