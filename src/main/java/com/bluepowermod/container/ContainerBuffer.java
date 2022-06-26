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

import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.tile.tier1.TileBuffer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class ContainerBuffer extends AbstractContainerMenu {
    
    private final Container inventory;
    
    public ContainerBuffer(int windowId, Inventory invPlayer, Container inventory) {
        super(BPMenuType.BUFFER.get(), windowId);
        this.inventory = inventory;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlot(new Slot(this.inventory, i * 5 + j, 45 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerBuffer(int windowId, Inventory invPlayer) {
        this(windowId, invPlayer, new SimpleContainer(TileBuffer.SLOTS));
    }
    
    protected void bindPlayerInventory(Inventory invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 162));
        }
    }
    
    @Override
    public boolean stillValid(Player player) {
    
        return inventory.stillValid(player);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int par2) {
    
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!moveItemStackTo(itemstack1, 20, 56, true)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(itemstack1, 0, 20, false)) { return ItemStack.EMPTY; }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() != itemstack.getCount()) {
                slot.onQuickCraft(itemstack, itemstack1);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }
}
