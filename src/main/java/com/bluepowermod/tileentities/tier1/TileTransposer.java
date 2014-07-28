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

package com.bluepowermod.tileentities.tier1;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.TileMachineBase;

import java.util.List;

public class TileTransposer extends TileMachineBase {
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        if (isBufferEmpty() && !worldObj.isRemote) {
            suckEntity();
        }
        
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);

        if (newValue) {
            suckItems();
            pullItem();
        }
        
    }
    
    private void pullItem() {
    
        if (isBufferEmpty()) {
            ForgeDirection dir = getOutputDirection().getOpposite();
            TileEntity inputTE = getTileCache()[dir.ordinal()].getTileEntity();
            ItemStack extractedStack = IOHelper.extractOneItem(inputTE, dir);
            if (extractedStack != null) addItemToOutputBuffer(extractedStack);
        }
    }
    
    private boolean suckItems() {
        ForgeDirection direction = getFacingDirection();
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, xCoord + direction.offsetX + 1, yCoord + direction.offsetY + 1, zCoord + direction.offsetZ + 1);

        // FIXME: This should work? -Q
        if (direction.offsetX != 0) {
            box = AxisAlignedBB.getBoundingBox(xCoord + direction.offsetX, yCoord -1, zCoord -1, xCoord + direction.offsetX, yCoord +1, zCoord +1);
        }
        if (direction.offsetY != 0) {
            box = AxisAlignedBB.getBoundingBox(xCoord -1, yCoord + direction.offsetY, zCoord -1, xCoord +1, yCoord + direction.offsetY, zCoord +1);
        }
        if (direction.offsetZ != 0) {
            box = AxisAlignedBB.getBoundingBox(xCoord -1, yCoord -1, zCoord + direction.offsetZ, xCoord +1, yCoord +1, zCoord + direction.offsetZ);
        }
//        BluePower.log.info(box.minX + "," + box.minY + "," + box.minZ + " - " + box.maxX + "," + box.maxY + "," + box.maxZ);
        if (!worldObj.getEntitiesWithinAABB(EntityItem.class, box).isEmpty()) {
            for (EntityItem entity : (List<EntityItem>)worldObj.getEntitiesWithinAABB(EntityItem.class, box)) {
                ItemStack stack = entity.getEntityItem();
                addItemToOutputBuffer(stack);
                entity.setDead();
                return true;
            }
        }
        return false;
    }

    private boolean suckEntity() {

        ForgeDirection direction = getFacingDirection();
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, xCoord + direction.offsetX + 1, yCoord + direction.offsetY + 1, zCoord + direction.offsetZ + 1);
        if (!worldObj.getEntitiesWithinAABB(EntityItem.class, box).isEmpty()) {
            for (EntityItem entity : (List<EntityItem>)worldObj.getEntitiesWithinAABB(EntityItem.class, box)) {
                ItemStack stack = entity.getEntityItem();
                addItemToOutputBuffer(stack);
                entity.setDead();
                return true;
            }
        }
        return false;
    }
}
