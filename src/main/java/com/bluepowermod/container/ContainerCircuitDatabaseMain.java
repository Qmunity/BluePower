/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.client.gui.GuiContainerBase;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCircuitDatabaseMain extends ContainerGhosts<TileCircuitDatabase> {

    private int curUploadProgress, curCopyProgress, selectedShareOption;

    public ContainerCircuitDatabaseMain(InventoryPlayer invPlayer, TileCircuitDatabase circuitDatabase) {
        super(circuitDatabase);
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

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (curUploadProgress != te.curUploadProgress) {
                icrafting.sendProgressBarUpdate(this, 0, te.curUploadProgress);
            }
            if (curCopyProgress != te.curCopyProgress) {
                icrafting.sendProgressBarUpdate(this, 1, te.curCopyProgress);
            }
            if (selectedShareOption != te.selectedShareOption) {
                icrafting.sendProgressBarUpdate(this, 2, te.selectedShareOption);
            }
        }
        curUploadProgress = te.curUploadProgress;
        curCopyProgress = te.curCopyProgress;
        selectedShareOption = te.selectedShareOption;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            te.curUploadProgress = value;
        }
        if (id == 1) {
            te.curCopyProgress = value;
        }
        if (id == 2) {
            te.selectedShareOption = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!mergeItemStack(itemstack1, 20, 55, false))
                    return null;
            } else {
                if (!mergeItemStack(itemstack1, 2, 20, false))
                    return null;
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
