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

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

public class InventoryItem extends SimpleContainer {
    
    private ItemStack    item;
    private Player player;
    private boolean      reading = false;
    
    public InventoryItem(Player player, ItemStack item, String name, boolean customName, int size) {
        super(item);
        this.player = player;
        this.item = item;
        
        if (!hasInventory()) {
            createInventory(player.level().registryAccess());
        }

        loadInventory(player.level().registryAccess());
    }
    
    public static InventoryItem getItemInventory(ItemStack is, String name, int size) {
    
        return getItemInventory(null, is, name, size);
    }
    
    public static InventoryItem getItemInventory(Player player, ItemStack is, String name, int size) {
    
        return new InventoryItem(player, is, name, false, size);
    }
    
    public ItemStack getItem() {
    
        return item;
    }

    @Override
    public void startOpen(Player player) {
        loadInventory(player.level().registryAccess());
    }

    @Override
    public void stopOpen(Player player) {
        super.stopOpen(player);
    }

    
    public void stopOpen(ItemStack is, HolderLookup.Provider provider) {
        saveInventory(is,  provider);
    }
    
    private boolean hasInventory() {
    
        if (!item.has(DataComponents.CUSTOM_DATA)) { return false; }
        return item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("Inventory");
    }
    
    private void createInventory(HolderLookup.Provider provider) {
    
        writeToNBT(provider);
    }
    
    protected void writeToNBT(HolderLookup.Provider provider) {
        ListTag itemList = new ListTag();
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                CompoundTag slotEntry = new CompoundTag();
                slotEntry.putByte("Slot", (byte) i);
                getItem(i).save(provider, slotEntry);
                itemList.add(slotEntry);
            }
        }
        CompoundTag inventory = new CompoundTag();
        inventory.put("Items", itemList);
        CompoundTag tag = new CompoundTag();
        tag.put("Inventory", inventory);
        item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
    
    public void loadInventory(HolderLookup.Provider provider) {
    
        readFromNBT(provider);
    }
    
    protected void setNBT(ItemStack is) {
    
        if (is.isEmpty() && player != null) {
            is = player.getMainHandItem();
        }
        
        if (!is.isEmpty() && is.getItem() == this.item.getItem()) {
            is.set(DataComponents.CUSTOM_DATA, item.get(DataComponents.CUSTOM_DATA));
        }
    }
    
    protected void readFromNBT(HolderLookup.Provider provider) {
    
        reading = true;
        
        ListTag itemList = (ListTag) ((CompoundTag) item.get(DataComponents.CUSTOM_DATA).copyTag().get("Inventory")).get("Items");
        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag slotEntry = itemList.getCompound(i);
            int j = slotEntry.getByte("Slot") & 0xff;
            
            if (j >= 0 && j < getContainerSize()) {
                setItem(j, ItemStack.parseOptional(provider, slotEntry));
            }
        }
        reading = false;
    }
    
    public void saveInventory(ItemStack is, HolderLookup.Provider provider) {
    
        writeToNBT(provider);
        setNBT(is);
    }
}
