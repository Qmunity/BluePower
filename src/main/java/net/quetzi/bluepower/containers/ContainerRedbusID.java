package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.quetzi.bluepower.tileentities.tier3.IRedBusWindow;
import net.quetzi.bluepower.tileentities.tier3.TileCPU;

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
