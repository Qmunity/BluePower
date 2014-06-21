package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.quetzi.bluepower.tileentities.tier3.TileDiskDrive;

public class ContainerDiskDrive extends Container {

	private final TileDiskDrive diskDrive;
	
	public ContainerDiskDrive(InventoryPlayer invPlayer, TileDiskDrive ent) {

		this.diskDrive = ent;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {

		return true;
	}

}
