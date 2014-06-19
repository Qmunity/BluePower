package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerDiskDrive;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.TileDiskDrive;

public class GuiDiskDrive extends GuiBase {
private final TileDiskDrive diskDrive;

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/diskdrivegui.png");

	public GuiDiskDrive (InventoryPlayer invPlayer, TileDiskDrive diskDrive) {
		super(new ContainerDiskDrive(invPlayer, diskDrive), resLoc);
		this.diskDrive = diskDrive;
	}
}
