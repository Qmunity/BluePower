package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerAlloyFurnace;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileEntities.tier1.TileAlloyFurnace;

public class GuiAlloyFurnace extends GuiBase {
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/GUI/alloy_furnace.png");
    private TileAlloyFurnace furnace;

	public GuiAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace _furnace) {
		super(new ContainerAlloyFurnace(invPlayer, _furnace), resLoc);
		furnace = _furnace;
	}

}
