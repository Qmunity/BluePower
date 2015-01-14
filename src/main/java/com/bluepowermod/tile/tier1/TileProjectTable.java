/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.lwjgl.Sys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.BluePower;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.tile.TileBase;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements IInventory, IGuiButtonSensitive {
    
    private ItemStack[]  inventory    = new ItemStack[18];
    private ItemStack[]  craftingGrid = new ItemStack[9];
    private static Field stackListFieldInventoryCrafting;
    
    public InventoryCrafting getCraftingGrid(Container listener) {
    
        InventoryCrafting inventoryCrafting = new InventoryCrafting(listener, 3, 3);
        if (stackListFieldInventoryCrafting == null) {
            stackListFieldInventoryCrafting = ReflectionHelper.findField(InventoryCrafting.class, "field_70466_a", "stackList");
        }
        try {
            stackListFieldInventoryCrafting.set(inventoryCrafting, craftingGrid);// Inject the array, so when stacks are being set to null by the
                                                                                 // container it'll make it's way over to the actual stacks.
            return inventoryCrafting;
        } catch (Exception e) {
            BluePower.log.error("This is about to go wrong, Project Table getCraftingGrid:");
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null) drops.add(stack);
        for (ItemStack stack : craftingGrid)
            if (stack != null) drops.add(stack);
        return drops;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);
        
        tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < craftingGrid.length; ++currentIndex) {
            if (craftingGrid[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                craftingGrid[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("CraftingGrid", tagList);
        
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    
        super.readFromNBT(tag);
        
        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[18];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
        
        tagList = tag.getTagList("CraftingGrid", 10);
        craftingGrid = new ItemStack[9];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < craftingGrid.length) {
                craftingGrid[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
    
        return inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return inventory[i];
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
    
        ItemStack itemStack = getStackInSlot(i);
        if (itemStack != null) {
            setInventorySlotContents(i, null);
        }
        return itemStack;
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        inventory[i] = itemStack;
    }
    
    @Override
    public String getInventoryName() {
    
        return BPBlocks.project_table.getUnlocalizedName();
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return false;
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
    
        return true;
    }
    
    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
        for (int i = 0; i < craftingGrid.length; i++) {
            if (craftingGrid[i] != null) {
                //craftingGrid[i] = IOHelper.insert(this, craftingGrid[i], ForgeDirection.UNKNOWN, false);
            	craftingGrid[i] = mergeItems(craftingGrid[i]);
            }
        }
    }
    
    public ItemStack mergeItems(ItemStack item) {
    	ItemStack gridResult = item.copy();
    	if (getNextFreeSlot() != -1 && !hasFreeStack(item)) { // No free stack to merge & Free slots -> go into first free slot
    		inventory[getNextFreeSlot()] = item;
    		gridResult = null;
    	} else if (getNextFreeSlot() != -1 && hasFreeStack(item)) {
    		if (item.stackSize + inventory[getFreeStack(item)].stackSize <= item.getMaxStackSize()) { // Free stack to merge on without opening up a new stack
    			int stacksize = item.stackSize + inventory[getFreeStack(item)].stackSize;
    			inventory[getFreeStack(item)] = item;
    			inventory[getFreeStack(item)].stackSize = stacksize;
    			gridResult = null;
    		} else { // Has a free stack to merge on but has to either open up a new stack or merge onto a second stack
    			int nextEqualSlot = getFreeStack(item);
    			int leftitems = (inventory[nextEqualSlot].stackSize + item.stackSize) - item.getMaxStackSize(); // Calculates how many items are left
    																											// 33 in the grid and 32 in the stack = 1 Item left
    			inventory[getFreeStack(item)].stackSize = item.getMaxStackSize();
    			if (hasFreeStack(item)) { // Free stack to merge and and has another free stack to merge left items on
        			ItemStack leftItemStack = item.copy(); // Gets the item that is cleared
        			leftItemStack.stackSize = leftitems;
        			mergeItems(leftItemStack);
        			gridResult = null;
    			} else { //Free stack to merge on but has to open up a new stack for leftover items
    				int nextFreeSlot = getNextFreeSlot();
    				inventory[nextFreeSlot] = item.copy();
    				inventory[nextFreeSlot].stackSize = leftitems;
	    			System.out.println(leftitems);
	    			gridResult = null;
    			}
 
    		}
    	} else if (getNextFreeSlot() == -1 && hasFreeStack(item)) { // Has at least one free stack to merge on but the table is full -> Old code works fine
    		gridResult = IOHelper.insert(this, item, ForgeDirection.UNKNOWN, false);
    	}
    	return gridResult;
    }
    

    
    public int getNextFreeSlot() {
    	for (int i = 0; i < inventory.length; i++) {
    		if (inventory[i] == null) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public boolean hasFreeStack(ItemStack item) {
    	for (int i = 0; i < inventory.length; i++) {
    		if (inventory[i] != null && inventory[i].getItem() == item.getItem() && inventory[i].stackSize < inventory[i].getMaxStackSize())
    			return true;
    	}
    	return false;
    }
    
    public int getFreeStack(ItemStack item) {
      	for (int i = 0; i < inventory.length; i++) {
    		if (inventory[i] != null && inventory[i].getItem() == item.getItem() && inventory[i].stackSize < inventory[i].getMaxStackSize())
    			return i;
    	}
    	return -1;
    }
}
