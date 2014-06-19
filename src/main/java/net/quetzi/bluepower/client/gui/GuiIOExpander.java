package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerIOExpander;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.TileIOExpander;

public class GuiIOExpander extends GuiBase {
private final TileIOExpander ioExpander;

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID+":textures/gui/ioexpandergui.png");

	public GuiIOExpander (InventoryPlayer invPlayer, TileIOExpander ioExpander) {
		super(new ContainerIOExpander (invPlayer, ioExpander), resLoc);
		this.ioExpander = ioExpander;
	}
}
