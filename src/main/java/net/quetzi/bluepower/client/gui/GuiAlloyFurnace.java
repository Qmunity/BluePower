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
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, furnace.getInventoryName(), true);
		
		
		
		checkTooltips(mouseX, mouseY);
	}
	

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		int burningPercentage = (int)(furnace.getBurningPercentage() * 12);
		
		//Todo: Tweak these variables a bit till it lines up perfectly.
        this.drawTexturedModalRect(x + 22, y + 54 + 12 - burningPercentage, 177, 11 - burningPercentage, 14, burningPercentage + 0);
	}

}
