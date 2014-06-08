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

package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.tileentities.TileBase;

import java.util.ArrayList;

public class TileBlockBreaker extends TileBase {
    @Override
    protected void redstoneChanged(boolean newValue) {
        super.redstoneChanged(newValue);

        if (newValue) {
            ForgeDirection direction = getFacingDirection();
            ForgeDirection oppDirection = direction.getOpposite();
            Block breakBlock = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            int breakMeta = worldObj.getBlockMetadata(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            ArrayList<ItemStack> breakStacks = breakBlock.getDrops(worldObj, xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, breakMeta, 0);
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + oppDirection.offsetX, yCoord + oppDirection.offsetY, zCoord + oppDirection.offsetZ);

            if (tileEntity instanceof IInventory) {
                IInventory inventory = (IInventory) tileEntity;
                worldObj.setBlockToAir(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ); //TODO: Not sure how to break with particles etc.
                for (ItemStack breakStack : breakStacks) {
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        ItemStack stack = inventory.getStackInSlot(i);
                        if (stack != null) {
                            if (stack.getItem() == breakStack.getItem() && stack.getItemDamage() == breakStack.getItemDamage() && stack.getMaxStackSize() - stack.stackSize > 0 ) {
                                stack.stackSize++;
                                return;
                            }
                        } else {
                            inventory.setInventorySlotContents(i, breakStack.copy());
                            return;
                        }
                    }
                }
            } else {
                worldObj.setBlockToAir(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ); //TODO: Not sure how to break with particles etc.

                for (ItemStack breakStack : breakStacks) {
                    float spawnX = xCoord + 0.5F + oppDirection.offsetX;
                    float spawnY = yCoord + 0.5F + oppDirection.offsetY;
                    float spawnZ = zCoord + 0.5F + oppDirection.offsetZ;

                    EntityItem droppedItem = new EntityItem(worldObj, spawnX, spawnY, spawnZ, breakStack);

                    droppedItem.motionX = (1 + 2 * oppDirection.offsetX) * 0.05F;
                    droppedItem.motionY = (3 + 2 * oppDirection.offsetY) * 0.05F;
                    droppedItem.motionZ = (1 + 2 * oppDirection.offsetZ) * 0.05F;

                    worldObj.spawnEntityInWorld(droppedItem);
                }
            }
        }
    }

    public ForgeDirection getFacingDirection() {
        return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }
}
