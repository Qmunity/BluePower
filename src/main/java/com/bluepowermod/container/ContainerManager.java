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
import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.tile.tier3.TileManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MineMaarten
 */
public class ContainerManager extends Container {

    private final IInventory manager;
    public TubeColor filterColor;
    public int priority = -1;
    public int mode = -1;
    public int fuzzySetting = -0;

    public ContainerManager(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.MANAGER, windowId);
        manager = inventory;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 6; ++j) {
                addSlot(new Slot(manager, j + i * 6, 44 + j * 18, 19 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerManager( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileManager.SLOTS ));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

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
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        if (id == 0) {
            filterColor = TubeColor.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 1) {
            priority = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 2) {
            mode = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if (id == 3) {
            fuzzySetting = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {

        return manager.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (par2 < 24) {
                if (!moveItemStackTo(itemstack1, 24, 60, false))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(itemstack1, 0, 24, false)) {
                return ItemStack.EMPTY;
            }
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
