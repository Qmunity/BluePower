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

package net.quetzi.bluepower.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiBase extends GuiContainer {

    private static final int           COLOR_TEXT  = 0xFFFFFFFF;
    protected            List<ToolTip> tooltipList = new ArrayList<ToolTip>();
    private ResourceLocation resLoc;

    public GuiBase(Container mainContainer, ResourceLocation _resLoc) {

        super(mainContainer);
        resLoc = _resLoc;
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {

        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + (h - height), xOffset + w, yOffset + h, color);
    }

    public void checkTooltips(int mouseX, int mouseY) {

        for (ToolTip tip : tooltipList) {
            if (shouldRenderToolTip(mouseX, mouseY, tip)) {
                drawTooltip(mouseX, mouseY, tip);
            }
        }
    }

    private void drawTooltip(int mouseX, int mouseY, ToolTip toDraw) {

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawHoveringText(toDraw.getText(), mouseX - x, mouseY - y, fontRendererObj);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private boolean shouldRenderToolTip(int mouseX, int mouseY, ToolTip theTip) {

        return func_146978_c(theTip.x, theTip.y, theTip.w, theTip.h, mouseX, mouseY);
    }

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = (w / 2) - (stringWidth / 2) + xOffset;
        }

        fontRendererObj.drawString(text, newX, yOffset, COLOR_TEXT, useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {

        fontRendererObj.drawString(text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }

    public void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color, String toolTipTitle,
            String toolTipUnit) {

        float perc = value / max;
        int height = (int) (h * perc);
        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        drawRect(xOffset, yOffset + (h - height), xOffset + w, yOffset + h, color);

        tooltipList.add(new ToolTip(xOffset, yOffset, w, h, toolTipTitle, toolTipUnit, value, max));
    }

    public void drawVerticalProgressBarWithTexture(int xOffset, int yOffset, int h, int w, float value, float max, IIcon icon, String toolTipTitle,
            String toolTipUnit) {

        float perc = value / max;
        int height = (int) (h * perc);
        float uMin = icon.getMinU();
        float uMax = icon.getMaxU();
        float vMin = icon.getMinV();
        float vMax = icon.getMaxV();
        float iconHeight = icon.getIconHeight();
        float icons = height / iconHeight;
        float vMaxLast = ((vMax - vMin) * (icons % 1.0F)) + vMin;

        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int o = 0;
        for (o = 0; o < Math.floor(icons); o++) {
            tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel, uMin, vMin); // BL
            tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel, uMax, vMin); // BR
            tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * (o + 1)), this.zLevel, uMax, vMax);
            tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * (o + 1)), this.zLevel, uMin, vMax);
        }
        o = (int) Math.floor(icons);

        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel, uMin, vMin); // BL
        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel, uMax, vMin); // BR
        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * (o + (icons % 1.0F))), this.zLevel, uMax, vMaxLast); // TR
        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * (o + (icons % 1.0F))), this.zLevel, uMin, vMaxLast); // TL

        tessellator.draw();

        mc.renderEngine.bindTexture(resLoc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        tooltipList.add(new ToolTip(xOffset, yOffset, w, h, toolTipTitle, toolTipUnit, value, max));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        tooltipList.clear();
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 94 + 2, COLOR_TEXT);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    public class ToolTip {

        int    x;
        int    y;
        int    w;
        int    h;
        String title;
        String unit;
        float  value;
        float  max;

        public ToolTip(int _x, int _y, int _w, int _h, String _title, String _unit, float _value, float _max) {

            x = _x;
            y = _y;
            w = _w;
            h = _h;

            title = _title;
            unit = _unit;
            value = _value;
            max = _max;
        }

        public List<String> getText() {

            List<String> text = new ArrayList<String>();
            text.add(title);
            text.add((int) value + "/" + (int) max + " " + unit);
            return text;
        }
    }

}
