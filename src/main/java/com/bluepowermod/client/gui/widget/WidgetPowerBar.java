package com.bluepowermod.client.gui.widget;

import com.bluepowermod.reference.Refs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.client.gui.widget.BaseWidget;

import java.util.List;

/**
 * @author K-4U (Koen Beckers)
 */
public class WidgetPowerBar extends BaseWidget {

    private float ampPercentage;
    private float ampStored;
    private float maxAmp;

    public WidgetPowerBar(int id, int x, int y, float ampStored_, float maxAmp_) {

        super(id, x, y, 7, 50, Refs.MODID + ":textures/gui/widgets/powerbar_widget_fill.png", Refs.MODID + ":textures/gui/widgets/powerbar_widget.png");
        ampPercentage = ampStored_ / maxAmp_;
        ampStored = ampStored_;
        maxAmp = maxAmp_;
    }

    @Override
    protected int getTextureV() {

        return 0;
    }

    @Override
    protected int getTextureWidth() {

        return 7;
    }

    @Override
    protected int getTextureHeight() {

        return 50;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        if (enabled) {
            GL11.glColor4d(1, 1, 1, 1);
        } else {
            GL11.glColor4d(0.2, 0.2, 0.2, 1);
        }
        //First, draw the background
        Minecraft.getMinecraft().getTextureManager().bindTexture(textures[1]);
        Gui.func_146110_a(x, y, getTextureU(), getTextureV(), width, height, 7, 50);

        //And then the fill..
        Minecraft.getMinecraft().getTextureManager().bindTexture(textures[0]);
        int h = (int)(((float)(height - 2)) * ampPercentage);
        Gui.func_146110_a(x+1, (y+height)-h-1, getTextureU(), getTextureV(), width-2, h, getTextureWidth()-2, getTextureHeight()-2);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
        curTip.add(ampStored + "/" + maxAmp + " mA");
    }
}
