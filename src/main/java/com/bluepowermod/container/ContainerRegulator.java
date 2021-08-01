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
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier2.TileRegulator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MineMaarten
 */
public class ContainerRegulator extends AbstractContainerMenu {

    private final Container regulator;
    private int filterColor = -1;
    public TubeColor color = TubeColor.BLACK;
    public int mode = -1;
    public int fuzzySetting = -1;

    public ContainerRegulator(int windowId, Inventory invPlayer, Container inventory) {
        super(BPContainerType.REGULATOR, windowId);
        this.regulator = inventory;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new SlotPhantom(regulator, j + i * 3, 8 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new Slot(regulator, j + i * 3 + 9, 80 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new SlotPhantom(regulator, j + i * 3 + 18, 152 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerRegulator( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileRegulator.SLOTS ));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 26 + j * 18, 86 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 26 + j * 18, 144));
        }
    }



    @Override
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        if (id == 0) {
            color = TubeColor.values()[value];
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
    public boolean stillValid(Player player) {

        return regulator.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (par2 >= 9 && par2 < 18) {
                if (!moveItemStackTo(itemstack1, 27, 63, true))
                    return ItemStack.EMPTY;
            } else if (par2 >= 27 && !moveItemStackTo(itemstack1, 9, 18, false)) {
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
