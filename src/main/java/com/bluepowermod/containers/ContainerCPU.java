package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import com.bluepowermod.tileentities.tier3.TileCPU;

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
