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
 *     @author Lumien
 */

package com.bluepowermod.container.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItem extends InventoryBasic {
    
    private ItemStack    item;
    private EntityPlayer player;
    
    private boolean      reading = false;
    
    public InventoryItem(EntityPlayer player, ItemStack item, String name, boolean customName, int size) {
    
        super(name, customName, size);
        
        this.player = player;
        this.item = item;
        
        if (!hasInventory()) {
            createInventory();
        }
    }
    
    public static InventoryItem getItemInventory(ItemStack is, String name, int size) {
    
        return getItemInventory(null, is, name, size);
    }
    
    public static InventoryItem getItemInventory(EntityPlayer player, ItemStack is, String name, int size) {
    
        return new InventoryItem(player, is, name, false, size);
    }
    
    public ItemStack getItem() {
    
        return item;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        loadInventory();
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        super.closeInventory(player);
    }

    
    public void closeInventory(ItemStack is) {
        saveInventory(is);
    }
    
    private boolean hasInventory() {
    
        if (item.getTagCompound() == null) { return false; }
        return item.getTagCompound().getTag("Inventory") != null;
    }
    
    private void createInventory() {
    
        writeToNBT();
    }
    
    protected void writeToNBT() {
    
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < getSizeInventory(); i++) {
            if (getStackInSlot(i) != null) {
                NBTTagCompound slotEntry = new NBTTagCompound();
                slotEntry.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(slotEntry);
                itemList.appendTag(slotEntry);
            }
        }
        NBTTagCompound inventory = new NBTTagCompound();
        inventory.setTag("Items", itemList);
        item.getTagCompound().setTag("Inventory", inventory);
    }
    
    public void loadInventory() {
    
        readFromNBT();
    }
    
    @Override
    public void markDirty() {
    
        super.markDirty();
        
        if (!reading) {
            saveInventory(null);
        }
    }
    
    protected void setNBT(ItemStack is) {
    
        if (is == null && player != null) {
            is = player.getHeldItemMainhand();
        }
        
        if (is != null && is.getItem() == this.item.getItem()) {
            is.setTagCompound(item.getTagCompound());
        }
    }
    
    protected void readFromNBT() {
    
        reading = true;
        
        NBTTagList itemList = (NBTTagList) ((NBTTagCompound) item.getTagCompound().getTag("Inventory")).getTag("Items");
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound slotEntry = itemList.getCompoundTagAt(i);
            int j = slotEntry.getByte("Slot") & 0xff;
            
            if (j >= 0 && j < getSizeInventory()) {
                setInventorySlotContents(j, new ItemStack(slotEntry));
            }
        }
        reading = false;
    }
    
    public void saveInventory(ItemStack is) {
    
        writeToNBT();
        setNBT(is);
    }
}
