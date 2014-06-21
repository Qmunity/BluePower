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

package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.tileentities.TileMachineBase;

import java.util.List;

public class TileTransposer extends TileMachineBase {
    
    private boolean isPowered;
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        if (isBufferEmpty()) {
            suckEntity(worldObj, xCoord,yCoord,zCoord);
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
    
    private void suckItems() {

        if (isBufferEmpty()) {

        }
    }

    private boolean suckEntity(World world, int x, int y, int z) {
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(0,0,0,1,1,1);
        if (!world.getEntitiesWithinAABB(EntityItem.class, box).isEmpty()) {
            for (Entity entity : (List<Entity>)world.getEntitiesWithinAABB(EntityItem.class, box)) {
                ItemStack stack = ((EntityItem)entity).getEntityItem();
                addItemToOutputBuffer(stack);
                entity.setDead();
            }
        }
        return false;
    }
}
