package net.quetzi.bluepower.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;

public abstract class GuiScreenBase extends GuiScreen {
    
    protected int guiLeft, guiTop, xSize, ySize;
    
    public GuiScreenBase(int xSize, int ySize) {
    
        this.xSize = xSize;
        this.ySize = ySize;
    }
    
    @Override
    public void initGui() {
    
        super.initGui();
        guiLeft = width / 2 - xSize / 2;
        guiTop = height / 2 - ySize / 2;
    }
    
    protected abstract ResourceLocation getTexture();
    
    @Override
    public void drawScreen(int x, int y, float partialTicks) {
    
        if (getTexture() != null) {
            drawDefaultBackground();
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(getTexture());
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
        super.drawScreen(x, y, partialTicks);
    }
}
