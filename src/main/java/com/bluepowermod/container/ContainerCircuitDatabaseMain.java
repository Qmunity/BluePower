/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.GuiContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCircuitDatabaseMain extends ContainerGhosts {

    private int curUploadProgress, curCopyProgress, selectedShareOption;
    private final TileCircuitDatabase circuitDatabase;

    public ContainerCircuitDatabaseMain(PlayerInventory invPlayer, TileCircuitDatabase circuitDatabase) {

        this.circuitDatabase = circuitDatabase;
        addSlotToContainer(new SlotPhantom(circuitDatabase.copyInventory, 0, 57, 64) {

            @Override
            public boolean isItemValid(ItemStack stack) {

                return stack.getItem() instanceof IDatabaseSaveable && ((IDatabaseSaveable) stack.getItem()).canGoInCopySlot(stack);
            }

            @Override
            public int getSlotStackLimit() {

                return 1;
            }
        });
        addSlotToContainer(new Slot(circuitDatabase.copyInventory, 1, 108, 64) {

            @Override
            public boolean isItemValid(ItemStack stack) {

                return stack.getItem() instanceof IDatabaseSaveable && ((IDatabaseSaveable) stack.getItem()).canGoInCopySlot(stack);
            }
        });

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(circuitDatabase, j + i * 9, 8 + j * 18, 95 + i * 18));
            }
        }

        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

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
    public boolean canInteractWith(PlayerEntity player) {

        return true;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;

            if (curUploadProgress != circuitDatabase.curUploadProgress) {
                icrafting.sendWindowProperty(this, 0, circuitDatabase.curUploadProgress);
            }
            if (curCopyProgress != circuitDatabase.curCopyProgress) {
                icrafting.sendWindowProperty(this, 1, circuitDatabase.curCopyProgress);
            }
            if (selectedShareOption != circuitDatabase.selectedShareOption) {
                icrafting.sendWindowProperty(this, 2, circuitDatabase.selectedShareOption);
            }
        }
        curUploadProgress = circuitDatabase.curUploadProgress;
        curCopyProgress = circuitDatabase.curCopyProgress;
        selectedShareOption = circuitDatabase.selectedShareOption;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            circuitDatabase.curUploadProgress = value;
        }
        if (id == 1) {
            circuitDatabase.curCopyProgress = value;
        }
        if (id == 2) {
            circuitDatabase.selectedShareOption = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!mergeItemStack(itemstack1, 20, 55, false))
                    return ItemStack.EMPTY;
            } else {
                if (!mergeItemStack(itemstack1, 2, 20, false))
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
