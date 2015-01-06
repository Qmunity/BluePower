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
 *     @author Quetzi
 */

package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.tile.tier1.TileFilter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerFilter extends Container {

    private final TileFilter tileFilter;
    private int filterColor = -1;
    private int fuzzySetting = -1;

    public ContainerFilter(InventoryPlayer invPlayer, TileFilter ejector) {

        tileFilter = ejector;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlotToContainer(new Slot(ejector, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (filterColor != tileFilter.filterColor.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 0, tileFilter.filterColor.ordinal());
            }

            if (fuzzySetting != tileFilter.fuzzySetting) {
                icrafting.sendProgressBarUpdate(this, 1, tileFilter.fuzzySetting);
            }
        }
        filterColor = tileFilter.filterColor.ordinal();
        fuzzySetting = tileFilter.fuzzySetting;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            tileFilter.filterColor = TubeColor.values()[value];
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 1) {
            tileFilter.fuzzySetting = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return tileFilter.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 9) {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                    return null;
            } else if (!mergeItemStack(itemstack1, 0, 9, false)) {
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
