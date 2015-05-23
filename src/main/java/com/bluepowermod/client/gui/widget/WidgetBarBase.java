package com.bluepowermod.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.widget.BaseWidget;

import com.bluepowermod.reference.Refs;

/**
 * @author K-4U (Koen Beckers)
 */
public abstract class WidgetBarBase extends BaseWidget {

    public WidgetBarBase(int x, int y) {

        super(-1, x, y, 7, 50, Refs.MODID + ":textures/gui/widgets/powerbar_widget_fill.png", Refs.MODID
                + ":textures/gui/widgets/powerbar_widget.png");

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
        int h = (int) ((height - 2) * getBarPercentage());
        Gui.func_146110_a(x + 1, y + height - h - 1, getTextureU(), getTextureV(), width - 2, h, getTextureWidth() - 2, getTextureHeight() - 2);
    }

    protected abstract double getBarPercentage();

}
