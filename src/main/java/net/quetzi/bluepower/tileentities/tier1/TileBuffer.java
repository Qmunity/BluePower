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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileBuffer extends TileBase implements IInventory {
    
    private ItemStack[] allInventories = new ItemStack[20];
    private ItemStack[] side1Inventory = new ItemStack[4];
    private ItemStack[] side2Inventory = new ItemStack[4];
    private ItemStack[] side3Inventory = new ItemStack[4];
    private ItemStack[] side4Inventory = new ItemStack[4];
    private ItemStack[] side5Inventory = new ItemStack[4];
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 20; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            allInventories[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 20; i++) {
            if (allInventories[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                allInventories[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }
    
    private ItemStack[] getInventoryForSide(int i) {
    
        if ((i >= 0) && (i < 4)) {
            for (int j = 0; j < 4; j++) {
                side1Inventory[j] = allInventories[i];
            }
            return side1Inventory;
        } else if ((i > 3) && (i < 8)) {
            for (int j = 0; j < 4; j++) {
                side2Inventory[j] = allInventories[i + 4];
            }
            return side2Inventory;
        } else if ((i > 7) && (i < 12)) {
            for (int j = 0; j < 4; j++) {
                side3Inventory[j] = allInventories[i + 8];
            }
            return side3Inventory;
        } else if ((i > 11) && (i < 16)) {
            for (int j = 0; j < 4; j++) {
                side4Inventory[j] = allInventories[i + 12];
            }
            return side4Inventory;
        } else if ((i > 15) && (i < 20)) {
            for (int j = 0; j < 4; j++) {
                side5Inventory[j] = allInventories[i + 16];
            }
            return side5Inventory;
        }
        return allInventories;
    }
    
    @Override
    public int getSizeInventory() {
    
        return allInventories.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return this.allInventories[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
    
        return getStackInSlot(i);
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
    }
    
    @Override
    public String getInventoryName() {
    
        return "tile.buffer.name";
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (stack != null) drops.add(stack);
        return drops;
    }
}
