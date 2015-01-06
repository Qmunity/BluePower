/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.tile.tier2.TileRetriever;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerRetriever extends ContainerFilter {

    private int slotIndex = -1, mode = -1;
    private final TileRetriever retriever;

    public ContainerRetriever(InventoryPlayer invPlayer, TileRetriever retriever) {

        super(invPlayer, retriever);
        this.retriever = retriever;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (slotIndex != retriever.slotIndex) {
                icrafting.sendProgressBarUpdate(this, 2, retriever.slotIndex);
            }
            if (mode != retriever.mode) {
                icrafting.sendProgressBarUpdate(this, 3, retriever.mode);
            }
        }
        slotIndex = retriever.slotIndex;
        mode = retriever.mode;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {

        super.updateProgressBar(id, value);

        if (id == 2) {
            retriever.slotIndex = value;
        }
        if (id == 3) {
            retriever.mode = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
    }
}
