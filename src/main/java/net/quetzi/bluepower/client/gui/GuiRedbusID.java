package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerRedbusID;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.IRedBusWindow;

public class GuiRedbusID extends GuiBase {
private final IRedBusWindow device;

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/redbusgui.png");

	public GuiRedbusID (InventoryPlayer invPlayer, IRedBusWindow device) {
		super(new ContainerRedbusID(invPlayer, device), resLoc);
		this.device = device;
	}
}
