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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class InventoryItem extends Inventory {
    
    private ItemStack    item;
    private PlayerEntity player;
    private boolean      reading = false;
    
    public InventoryItem(PlayerEntity player, ItemStack item, String name, boolean customName, int size) {
        super(item);
        this.player = player;
        this.item = item;
        
        if (!hasInventory()) {
            createInventory();
        }

        loadInventory();
    }
    
    public static InventoryItem getItemInventory(ItemStack is, String name, int size) {
    
        return getItemInventory(null, is, name, size);
    }
    
    public static InventoryItem getItemInventory(PlayerEntity player, ItemStack is, String name, int size) {
    
        return new InventoryItem(player, is, name, false, size);
    }
    
    public ItemStack getItem() {
    
        return item;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        loadInventory();
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        super.closeInventory(player);
    }

    
    public void closeInventory(ItemStack is) {
        saveInventory(is);
    }
    
    private boolean hasInventory() {
    
        if (item.getTag() == null) { return false; }
        return item.getTag().contains("Inventory");
    }
    
    private void createInventory() {
    
        writeToNBT();
    }
    
    protected void writeToNBT() {
    
        if (item.getTag() == null) {
            item.setTag(new CompoundNBT());
        }
        ListNBT itemList = new ListNBT();
        for (int i = 0; i < getSizeInventory(); i++) {
            if (!getStackInSlot(i).isEmpty()) {
                CompoundNBT slotEntry = new CompoundNBT();
                slotEntry.putByte("Slot", (byte) i);
                getStackInSlot(i).write(slotEntry);
                itemList.add(slotEntry);
            }
        }
        CompoundNBT inventory = new CompoundNBT();
        inventory.put("Items", itemList);
        item.getTag().put("Inventory", inventory);
    }
    
    public void loadInventory() {
    
        readFromNBT();
    }
    
    @Override
    public void markDirty() {
    
        super.markDirty();
        
        if (!reading) {
            saveInventory(ItemStack.EMPTY);
        }
    }
    
    protected void setNBT(ItemStack is) {
    
        if (is.isEmpty() && player != null) {
            is = player.getHeldItemMainhand();
        }
        
        if (!is.isEmpty() && is.getItem() == this.item.getItem()) {
            is.setTag(item.getTag());
        }
    }
    
    protected void readFromNBT() {
    
        reading = true;
        
        ListNBT itemList = (ListNBT) ((CompoundNBT) item.getTag().get("Inventory")).get("Items");
        for (int i = 0; i < itemList.size(); i++) {
            CompoundNBT slotEntry = itemList.getCompound(i);
            int j = slotEntry.getByte("Slot") & 0xff;
            
            if (j >= 0 && j < getSizeInventory()) {
                setInventorySlotContents(j, ItemStack.read(slotEntry));
            }
        }
        reading = false;
    }
    
    public void saveInventory(ItemStack is) {
    
        writeToNBT();
        setNBT(is);
    }
}
