/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.containers.slots.SlotCircuitTableCrafting;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;

public class ContainerCircuitTable extends Container {
    
    private final TileCircuitTable circuitTable;
    private int                    itemsCrafted;
    private boolean                isRetrying = false;
    
    public ContainerCircuitTable(InventoryPlayer invPlayer, TileCircuitTable circuitTable) {
    
        this.circuitTable = circuitTable;
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 8; ++j) {
                addSlotToContainer(new SlotCircuitTableCrafting(invPlayer.player, circuitTable, circuitTable.circuitInventory, j + i * 8, 8 + j * 18, 33 + i * 18));
            }
        }
        
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(circuitTable, j + i * 9, 8 + j * 18, 95 + i * 18));
            }
        }
        
        bindPlayerInventory(invPlayer);
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 142 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 200));
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    protected void retrySlotClick(int slot, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
    
        ItemStack stackInSlot = ((Slot) inventorySlots.get(slot)).getStack();
        itemsCrafted += stackInSlot.stackSize;
        isRetrying = true;
        if (itemsCrafted < stackInSlot.getMaxStackSize()) {
            slotClick(slot, p_75133_2_, 1, p_75133_4_);//only crafting slot doesn't retry clicking so no more than 64 items get crafted at a time
        }
        isRetrying = false;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
    
        if (!isRetrying) itemsCrafted = 0;
        
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 42) {
                if (!mergeItemStack(itemstack1, 42, 77, false)) return null;
            } else {
                if (!mergeItemStack(itemstack1, 24, 42, false)) return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize) {
                slot.onPickupFromSlot(player, itemstack1);
            } else {
                return null;
            }
        }
        return itemstack;
    }
    
}
