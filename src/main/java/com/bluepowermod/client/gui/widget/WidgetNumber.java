package com.bluepowermod.client.gui.widget;

import net.minecraft.client.Minecraft;

import com.bluepowermod.util.Refs;

public class WidgetNumber extends WidgetMode {
    
    public WidgetNumber(int id, int x, int y, int maxNumber) {
    
        super(id, x, y, 0, maxNumber, Refs.MODID + ":textures/gui/widgets/empty_widget.png");
    }
    
    @Override
    public void render(int mouseX, int mouseY) {
    
        super.render(mouseX, mouseY);
        Minecraft.getMinecraft().fontRenderer.drawString("" + value, x + 4, y + 3, 4210752);
    }
    
    @Override
    protected int getTextureV() {
    
        return 0;
    }
    
    @Override
    protected int getTextureWidth() {
    
        return 14;
    }
    
    @Override
    protected int getTextureHeight() {
    
        return 14;
    }
}
