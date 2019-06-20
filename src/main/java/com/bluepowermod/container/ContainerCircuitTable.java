/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.container.slot.SlotCircuitTableCrafting;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;


//@ChestContainer
public class ContainerCircuitTable extends Container {

    private final IInventory circuitTable;
    public CraftingInventory craftMatrix;
    private int itemsCrafted;
    private boolean isRetrying = false;
    private int scrollState = -1;

    public ContainerCircuitTable(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.CIRCUIT_TABLE, windowId);
        this.circuitTable = inventory;
        craftMatrix = new CraftingInventory(this, 5, 5);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 8; ++j) {
                addSlot(new SlotCircuitTableCrafting(invPlayer.player, circuitTable, craftMatrix, j + i * 8, 8 + j * 18,
                        33 + i * 18));
            }
        }

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(circuitTable, j + i * 9, 8 + j * 18, 95 + i * 18));
            }
        }

        bindPlayerInventory(invPlayer);
    }

    public ContainerCircuitTable( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileCircuitTable.SLOTS ));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 142 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 200));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {

        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {

        if (!isRetrying)
            itemsCrafted = 0;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 42) {
                if (!mergeItemStack(itemstack1, 42, 77, false))
                    return ItemStack.EMPTY;
            } else {
                if (!mergeItemStack(itemstack1, 24, 42, false))
                    return ItemStack.EMPTY;
            }
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
