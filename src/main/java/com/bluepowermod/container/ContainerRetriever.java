/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.tile.tier2.TileRetriever;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;

import com.bluepowermod.ClientProxy;

import net.minecraft.inventory.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MineMaarten
 */
public class ContainerRetriever extends ContainerFilter {

    public int slotIndex = -1;
    public int mode = -1;
    private final IInventory retriever;

    public ContainerRetriever(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.RETRIEVER, windowId, inventory);
        this.retriever = inventory;
    }

    public ContainerRetriever( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileRetriever.SLOTS ));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        super.setData(id, value);

        if (id == 2) {
            slotIndex = value;
        }
        if (id == 3) {
            mode = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }
}
