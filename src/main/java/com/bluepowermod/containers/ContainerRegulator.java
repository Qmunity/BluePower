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

package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.containers.slots.SlotPhantom;
import com.bluepowermod.tileentities.tier2.TileRegulator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerRegulator extends ContainerGhosts {

    private final TileRegulator tileRegulator;
    private int filterColor = -1;
    private int mode = -1;
    private int fuzzySetting = -1;

    public ContainerRegulator(InventoryPlayer invPlayer, TileRegulator regulator) {

        tileRegulator = regulator;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlotToContainer(new SlotPhantom(regulator, j + i * 3, 8 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlotToContainer(new Slot(regulator, j + i * 3 + 9, 80 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlotToContainer(new SlotPhantom(regulator, j + i * 3 + 18, 152 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 26 + j * 18, 86 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 26 + j * 18, 144));
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

            if (filterColor != tileRegulator.color.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 0, tileRegulator.color.ordinal());
            }
            if (mode != tileRegulator.mode) {
                icrafting.sendProgressBarUpdate(this, 2, tileRegulator.mode);
            }
            if (fuzzySetting != tileRegulator.fuzzySetting) {
                icrafting.sendProgressBarUpdate(this, 3, tileRegulator.fuzzySetting);
            }
        }
        filterColor = tileRegulator.color.ordinal();
        mode = tileRegulator.mode;
        fuzzySetting = tileRegulator.fuzzySetting;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            tileRegulator.color = TubeColor.values()[value];
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 2) {
            tileRegulator.mode = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 3) {
            tileRegulator.fuzzySetting = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return tileRegulator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 >= 9 && par2 < 18) {
                if (!mergeItemStack(itemstack1, 27, 63, true))
                    return null;
            } else if (par2 >= 27 && !mergeItemStack(itemstack1, 9, 18, false)) {
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
