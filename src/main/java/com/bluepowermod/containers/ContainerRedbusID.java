package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import com.bluepowermod.tileentities.tier3.IRedBusWindow;

public class ContainerRedbusID extends Container {

	private final IRedBusWindow device;
	
	public ContainerRedbusID(InventoryPlayer invPlayer, IRedBusWindow device) {

		this.device = device;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {

		return true;
	}

}
