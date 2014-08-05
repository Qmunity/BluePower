package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.bluepowermod.api.Refs;
import com.bluepowermod.containers.ContainerRedbusID;
import com.bluepowermod.tileentities.tier3.IRedBusWindow;

public class GuiRedbusID extends GuiBase {
private final IRedBusWindow device;

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/redbusgui.png");

	public GuiRedbusID (InventoryPlayer invPlayer, IRedBusWindow device) {
		super(new ContainerRedbusID(invPlayer, device), resLoc);
		this.device = device;
		
		this.xSize = 123;
		this.ySize = 81;
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawHorizontalAlignedString(7, 4, xSize - 14, StatCollector.translateToLocal("gui.redbusgui"), true);
        
        drawHorizontalAlignedString(7, 60, xSize - 14, StatCollector.translateToLocal("gui.redbus.id") + ":" + device.redbus_id, true);
    }
	
	//TODO: clicking on switches toggles state and updates redbus_id
}
