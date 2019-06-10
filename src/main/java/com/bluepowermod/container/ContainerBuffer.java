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

package com.bluepowermod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.tile.tier1.TileBuffer;

public class ContainerBuffer extends Container {
    
    private final TileBuffer tileBuffer;
    
    public ContainerBuffer(PlayerInventory invPlayer, TileBuffer buffer) {
    
        tileBuffer = buffer;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlotToContainer(new Slot(buffer, i * 5 + j, 45 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }
    
    protected void bindPlayerInventory(PlayerInventory invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 162));
        }
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity player) {
    
        return tileBuffer.isUsableByPlayer(player);
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {
    
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!mergeItemStack(itemstack1, 20, 56, true)) return ItemStack.EMPTY;
            } else if (!mergeItemStack(itemstack1, 0, 20, false)) { return ItemStack.EMPTY; }
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() != itemstack.getCount()) {
                slot.onSlotChange(itemstack, itemstack1);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }
}
