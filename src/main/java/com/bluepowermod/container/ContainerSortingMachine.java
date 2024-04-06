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
 */

package com.bluepowermod.container;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Arrays;

/**
 * @author MineMaarten
 */
public class ContainerSortingMachine extends AbstractContainerMenu {

    private final Container inventory;

    public TileSortingMachine.PullMode pullMode = TileSortingMachine.PullMode.AUTOMATIC;
    public TileSortingMachine.SortMode sortMode = TileSortingMachine.SortMode.ANY_ITEM;
    public int curColumn = -1;
    public final TubeColor[] colors = new TubeColor[9];
    public final int[] fuzzySettings = new int[8];

    public ContainerSortingMachine(int windowId, Inventory invPlayer, Container inventory) {
        super(BPMenuType.SORTING_MACHINE.get(), windowId);
        this.inventory = inventory;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                addSlot(new SlotPhantom(this.inventory, i * 8 + j, 26 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        //Arrays.fill(colors, -1);
        Arrays.fill(fuzzySettings, -1);

    }

    public ContainerSortingMachine(int windowId, Inventory invPlayer) {
        this(windowId, invPlayer, new SimpleContainer(TileSortingMachine.SLOTS));
    }

    protected void bindPlayerInventory(Inventory invPlayer) {

        int offset = 157;
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, offset + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 58 + offset));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int par2) {
        return ItemStack.EMPTY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        if (id < 9) {
            colors[id] = TubeColor.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 9) {
            pullMode = TileSortingMachine.PullMode.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 10) {
            sortMode = TileSortingMachine.SortMode.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 11) {
            curColumn = value;
        }

        if (id >= 12 && id < 21) {
            fuzzySettings[id - 12] = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return inventory.stillValid(entityplayer);
    }

}
