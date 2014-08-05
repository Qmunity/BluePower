package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import com.bluepowermod.tileentities.tier3.TileMonitor;

public class ContainerMonitor extends Container {

	public ContainerMonitor(InventoryPlayer inventoryPlayer, TileMonitor monitor) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
