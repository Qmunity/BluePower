/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import com.bluepowermod.tile.tier3.TileMonitor;
import net.minecraft.world.item.ItemStack;

public class ContainerMonitor extends AbstractContainerMenu {

	private final Container inventory;

	public ContainerMonitor(int windowId, Inventory invPlayer, Container inventory) {
		super(BPMenuType.MONITOR.get(), windowId);
		this.inventory = inventory;
	}

	public ContainerMonitor( int id, Inventory player )    {
		this( id, player, new SimpleContainer( 0 ));
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return null;
	}

	@Override
	public boolean stillValid(Player playerEntity) {
		return inventory.stillValid(playerEntity);
	}
}
