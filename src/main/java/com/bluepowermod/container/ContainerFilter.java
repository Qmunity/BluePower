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
import com.bluepowermod.tile.tier1.TileFilter;
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
public class ContainerFilter extends ContainerMachineBase {

    private final TileFilter tileFilter;
    private int filterColor = -1;
    private int fuzzySetting = -1;

    public ContainerFilter(PlayerInventory invPlayer, TileFilter filter) {

        super(filter);
        tileFilter = filter;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlotToContainer(new Slot(filter, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

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

        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;

            if (filterColor != tileFilter.filterColor.ordinal()) {
                icrafting.sendWindowProperty(this, 0, tileFilter.filterColor.ordinal());
            }

            if (fuzzySetting != tileFilter.fuzzySetting) {
                icrafting.sendWindowProperty(this, 1, tileFilter.fuzzySetting);
            }
        }
        filterColor = tileFilter.filterColor.ordinal();
        fuzzySetting = tileFilter.fuzzySetting;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id == 0) {
            tileFilter.filterColor = TubeColor.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 1) {
            tileFilter.fuzzySetting = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {

        return tileFilter.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 9) {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(itemstack1, 0, 9, false)) {
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
