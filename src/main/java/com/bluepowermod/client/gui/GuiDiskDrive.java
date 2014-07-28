package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import com.bluepowermod.containers.ContainerDiskDrive;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier3.TileDiskDrive;

public class GuiDiskDrive extends GuiBase {
private final TileDiskDrive diskDrive;

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/diskdrivegui.png");

	public GuiDiskDrive (InventoryPlayer invPlayer, TileDiskDrive diskDrive) {
		super(new ContainerDiskDrive(invPlayer, diskDrive), resLoc);
		this.diskDrive = diskDrive;
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }
}
