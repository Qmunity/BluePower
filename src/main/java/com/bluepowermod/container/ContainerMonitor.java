/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;

import com.bluepowermod.tile.tier3.TileMonitor;

public class ContainerMonitor extends Container {

	private final IInventory inventory;

	public ContainerMonitor(int windowId, PlayerInventory invPlayer, IInventory inventory) {
		super(BPContainerType.MONITOR, windowId);
		this.inventory = inventory;
	}

	public ContainerMonitor( int id, PlayerInventory player )    {
		this( id, player, new Inventory( 0 ));
	}

	@Override
	public boolean stillValid(PlayerEntity playerEntity) {
		return inventory.stillValid(playerEntity);
	}
}
