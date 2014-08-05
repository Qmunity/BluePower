package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import com.bluepowermod.tileentities.tier3.TileIOExpander;

public class ContainerIOExpander extends Container {

	public ContainerIOExpander(InventoryPlayer inventoryPlayer, TileIOExpander ioExpander) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
