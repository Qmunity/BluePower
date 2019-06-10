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

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.tile.tier3.TileManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerManager extends ContainerMachineBase {

    private final TileManager tileManager;
    private int filterColor = -1;
    private int priority = -1;
    private int mode = -1;
    private int fuzzySetting = -0;

    public ContainerManager(PlayerInventory invPlayer, TileManager manager) {

        super(manager);
        tileManager = manager;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 6; ++j) {
                addSlotToContainer(new Slot(manager, j + i * 6, 44 + j * 18, 19 + i * 18));
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

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;

            if (filterColor != tileManager.filterColor.ordinal()) {
                icrafting.sendWindowProperty(this, 0, tileManager.filterColor.ordinal());
            }
            if (priority != tileManager.priority) {
                icrafting.sendWindowProperty(this, 1, tileManager.priority);
            }
            if (mode != tileManager.mode) {
                icrafting.sendWindowProperty(this, 2, tileManager.mode);
            }
            if (fuzzySetting != tileManager.fuzzySetting) {
                icrafting.sendWindowProperty(this, 3, tileManager.fuzzySetting);
            }
        }
        filterColor = tileManager.filterColor.ordinal();
        priority = tileManager.priority;
        mode = tileManager.mode;
        fuzzySetting = tileManager.fuzzySetting;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            tileManager.filterColor = TubeColor.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 1) {
            tileManager.priority = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 2) {
            tileManager.mode = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 3) {
            tileManager.fuzzySetting = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {

        return tileManager.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 24) {
                if (!mergeItemStack(itemstack1, 24, 60, false))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(itemstack1, 0, 24, false)) {
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
