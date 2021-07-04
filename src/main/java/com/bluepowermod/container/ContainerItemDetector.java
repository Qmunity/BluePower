/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.tile.tier1.TileItemDetector;
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
public class ContainerItemDetector extends Container {

    public int mode = -1;
    public int fuzzySetting = -1;
    private final IInventory itemDetector;

    public ContainerItemDetector(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.ITEM_DETECTOR, windowId);
        this.itemDetector = inventory;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new Slot(itemDetector, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerItemDetector( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileItemDetector.SLOTS ));
    }


    protected void bindPlayerInventory(PlayerInventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {

        return itemDetector.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (par2 < 9) {
                if (!moveItemStackTo(itemstack1, 9, 45, true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(itemstack1, 0, 9, false)) {
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


    @Override
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        super.setData(id, value);
        if (id == 0) {
            mode = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        } else if (id == 1) {
            fuzzySetting = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

}
