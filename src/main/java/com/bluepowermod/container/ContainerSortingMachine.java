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
import com.bluepowermod.container.slot.SlotPhantom;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.client.gui.GuiContainerBase;

import java.util.Arrays;

/**
 *
 * @author MineMaarten
 */
public class ContainerSortingMachine extends ContainerMachineBase {

    private final TileSortingMachine sortingMachine;

    private int pullMode = -1, sortMode = -1, curColumn = -1;
    private final int[] colors = new int[9];
    private final int[] fuzzySettings = new int[8];

    private final static int AMPSTORED = 22;
    private final static int AMPMAX = 23;
    public float ampStored;
    public float ampMax;


    public ContainerSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {

        super(sortingMachine);
        this.sortingMachine = sortingMachine;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                addSlotToContainer(new SlotPhantom(sortingMachine, i * 8 + j, 26 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        Arrays.fill(colors, -1);
        Arrays.fill(fuzzySettings, -1);

    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        int offset = 157;
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, offset + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 58 + offset));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {

        return null;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            for (int i = 0; i < 9; i++) {
                if (colors[i] != sortingMachine.colors[i].ordinal()) {
                    icrafting.sendProgressBarUpdate(this, i, sortingMachine.colors[i].ordinal());
                }
            }

            if (pullMode != sortingMachine.pullMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 9, sortingMachine.pullMode.ordinal());
            }

            if (sortMode != sortingMachine.sortMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 10, sortingMachine.sortMode.ordinal());
            }

            if (curColumn != sortingMachine.curColumn) {
                icrafting.sendProgressBarUpdate(this, 11, sortingMachine.curColumn);
            }

            for (int i = 0; i < 8; i++) {
                if (fuzzySettings[i] != sortingMachine.fuzzySettings[i]) {
                    icrafting.sendProgressBarUpdate(this, i + 12, sortingMachine.fuzzySettings[i]);
                }
            }

            if (ampStored != sortingMachine.getHandler().getAmpStored()) {
                icrafting.sendProgressBarUpdate(this, AMPSTORED, (int)sortingMachine.getHandler().getAmpStored());
            }

            if (ampMax != sortingMachine.getHandler().getMaxAmp()) {
                icrafting.sendProgressBarUpdate(this, AMPMAX, (int)sortingMachine.getHandler().getMaxAmp());
            }
        }

        pullMode = sortingMachine.pullMode.ordinal();
        sortMode = sortingMachine.sortMode.ordinal();
        curColumn = sortingMachine.curColumn;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = sortingMachine.colors[i].ordinal();
        }
        for (int i = 0; i < fuzzySettings.length; i++) {
            fuzzySettings[i] = sortingMachine.fuzzySettings[i];
        }
        ampStored = sortingMachine.getHandler().getAmpStored();
        ampMax = sortingMachine.getHandler().getMaxAmp();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        if (id < 9) {
            sortingMachine.colors[id] = TubeColor.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 9) {
            sortingMachine.pullMode = TileSortingMachine.PullMode.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 10) {
            sortingMachine.sortMode = TileSortingMachine.SortMode.values()[value];
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == 11) {
            sortingMachine.curColumn = value;
        }

        if (id >= 12 && id < 21) {
            sortingMachine.fuzzySettings[id - 12] = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

        if (id == AMPSTORED) {
            sortingMachine.setAmpStored(value);
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if(id == AMPMAX){
            sortingMachine.setMaxAmp(value);
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {

        return sortingMachine.isUseableByPlayer(entityplayer);
    }

}
