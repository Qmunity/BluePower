package com.bluepowermod.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.Rectangle;
import java.util.List;

public interface IGuiWidget {

    public void setListener(IWidgetListener gui);

    public int getID();

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    public void onMouseClicked(int mouseX, int mouseY, int button);

    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed);

    public void update();

    public Rectangle getBounds();
}
