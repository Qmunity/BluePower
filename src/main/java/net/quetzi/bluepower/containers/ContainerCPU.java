package net.quetzi.bluepower.containers;

import net.quetzi.bluepower.tileentities.tier3.TileCPU;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerCPU extends Container {

	private final TileCPU cpu;
	
	public ContainerCPU(InventoryPlayer invPlayer, TileCPU cpu) {

		this.cpu = cpu;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {

		return true;
	}

}
