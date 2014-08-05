/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.IWidgetListener;

public class GuiBase extends GuiContainer implements IWidgetListener {
    
    private static final int       COLOR_TEXT = 4210752;
    private final List<IGuiWidget> widgets    = new ArrayList<IGuiWidget>();
    private final ResourceLocation resLoc;
    
    public GuiBase(Container mainContainer, ResourceLocation _resLoc) {
    
        super(mainContainer);
        resLoc = _resLoc;
    }
    
    protected void addWidget(IGuiWidget widget) {
    
        widgets.add(widget);
        widget.setListener(this);
    }
    
    @Override
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {
    
        widgets.clear();
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }
    
    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {
    
        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + h - height, xOffset + w, yOffset + h, color);
    }
    
    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {
    
        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }
        
        fontRendererObj.drawString(text, newX, yOffset, COLOR_TEXT, useShadow);
    }
    
    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {
    
        fontRendererObj.drawString(text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 94 + 2, COLOR_TEXT);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);
        
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        for (IGuiWidget widget : widgets) {
            widget.render(i, j);
        }
    }
    
    @Override
    public void drawScreen(int x, int y, float partialTick) {
    
        super.drawScreen(x, y, partialTick);
        List<String> tooltip = new ArrayList<String>();
        boolean shift = BluePower.proxy.isSneakingInGui();
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y)) widget.addTooltip(tooltip, shift);
        }
        if (!tooltip.isEmpty()) {
            List<String> localizedTooltip = new ArrayList<String>();
            for (String line : tooltip) {
                String localizedLine = I18n.format(line);
                String[] lines = WordUtils.wrap(localizedLine, 50).split(System.getProperty("line.separator"));
                for (String locLine : lines) {
                    localizedTooltip.add(locLine);
                }
            }
            drawHoveringText(localizedTooltip, x, y, fontRendererObj);
        }
        
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) {
    
        super.mouseClicked(x, y, button);
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y)) widget.onMouseClicked(x, y, button);
        }
    }
    
    @Override
    public void actionPerformed(IGuiWidget widget) {
    
    }
    
    public void redraw() {
    
        buttonList.clear();
        widgets.clear();
        initGui();
    }
}
