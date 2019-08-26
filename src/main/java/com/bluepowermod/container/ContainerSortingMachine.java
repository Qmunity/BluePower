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
import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

/**
 * @author MineMaarten
 */
public class ContainerSortingMachine extends Container {

    private final IInventory inventory;

    public TileSortingMachine.PullMode pullMode = TileSortingMachine.PullMode.AUTOMATIC;
    public TileSortingMachine.SortMode sortMode = TileSortingMachine.SortMode.ANY_ITEM;
    public int curColumn = -1;
    public final TubeColor[] colors = new TubeColor[9];
    public final int[] fuzzySettings = new int[8];

    public ContainerSortingMachine(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.SORTING_MACHINE, windowId);
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

    public ContainerSortingMachine(int windowId, PlayerInventory invPlayer) {
        this(windowId, invPlayer, new Inventory(TileSortingMachine.SLOTS));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

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
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2) {

        return ItemStack.EMPTY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int value) {

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
    public boolean canInteractWith(PlayerEntity entityplayer) {
        return inventory.isUsableByPlayer(entityplayer);
    }

}
