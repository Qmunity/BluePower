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

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.BluePower;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.TileMachineBase;

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
    
    private static AxisAlignedBB[] ITEM_SUCK_AABBS;
    static{
        ITEM_SUCK_AABBS = new AxisAlignedBB[6];
        ITEM_SUCK_AABBS[0] = AxisAlignedBB.getBoundingBox(-1, -1, -1, 2, 0, 2);
        ITEM_SUCK_AABBS[1] = AxisAlignedBB.getBoundingBox(-1, 1, -1, 2, 2, 2);
        ITEM_SUCK_AABBS[2] = AxisAlignedBB.getBoundingBox(-1, -1, -1, 2, 2, 0);
        ITEM_SUCK_AABBS[3] = AxisAlignedBB.getBoundingBox(-1, -1, 1, 2, 2, 2);
        ITEM_SUCK_AABBS[4] = AxisAlignedBB.getBoundingBox(-1, -1, -1, 0, 2, 2);
        ITEM_SUCK_AABBS[5] = AxisAlignedBB.getBoundingBox(1, -1, -1, 2, 2, 2);
    }
    
    private boolean suckItems() {
        for (EntityItem entity : (List<EntityItem>)worldObj.getEntitiesWithinAABB(EntityItem.class, ITEM_SUCK_AABBS[getFacingDirection().ordinal()].copy().offset(xCoord, yCoord, zCoord))) {
            ItemStack stack = entity.getEntityItem();
            addItemToOutputBuffer(stack);
            entity.setDead();
            return true;
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
