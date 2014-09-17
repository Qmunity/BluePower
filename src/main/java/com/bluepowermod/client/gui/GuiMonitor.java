/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.containers.ContainerMonitor;
import com.bluepowermod.tileentities.tier3.TileMonitor;
import com.bluepowermod.util.Refs;

public class GuiMonitor extends GuiBase {
    
    private static final ResourceLocation resLoc            = new ResourceLocation(Refs.MODID + ":textures/gui/monitorgui.png");
    private static final ResourceLocation chracterSetResLoc = new ResourceLocation(Refs.MODID + ":textures/gui/65el02_chars.png");
    private final TileMonitor             monitor;
    
    public GuiMonitor(InventoryPlayer invPlayer, TileMonitor monitor) {
    
        super(new ContainerMonitor(invPlayer, monitor), resLoc);
        this.monitor = monitor;
        xSize = 350;
        ySize = 230;
        width = 350 / 2;
        // TODO: fix height and width fields as well
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(resLoc);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect2(k, l, 0, 0, this.xSize, this.ySize);
        
        // screen color
        this.mc.getTextureManager().bindTexture(chracterSetResLoc);
        GL11.glColor4f(monitor.screenColor[0], monitor.screenColor[1], monitor.screenColor[2], 1.0F);
        
        for (int row = 0; row < 50; row++) {
            for (int col = 0; col < 80; col++) {
                byte character = monitor.screenMemory[row * 80 + col];
                // TODO: overlay cursor character
                if (character != 32) {
                    drawCharacter(row, col, character);
                }
            }
        }
    }
    
    private void drawCharacter(int row, int col, byte character) {
    
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        int tempOffset = 0; // 350;
        if (monitor.mode80x40) {
            // Not implemented yet
            drawTexturedModalRect3(x + 15 + col * 4, y + 15 + row * 4, tempOffset + (character & 0xF) * 8, (character >> 4) * 8, 8, 8);
        } else {
            // TODO: fix texture mapping issues
            drawTexturedModalRect3(x + 15 + col * 4, y + 15 + row * 4, tempOffset + (character & 0xF) * 8, (character >> 4) * 8, 8, 8);
        }
    }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    @SuppressWarnings("cast")
    public void drawTexturedModalRect2(int x, int z, int u, int v, int w, int h) {
    
        float f = 0.00195313F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x + 0), (double) (z + h), (double) this.zLevel, (double) ((float) (u + 0) * f),
                (double) ((float) (v + h) * f1));
        tessellator.addVertexWithUV((double) (x + w), (double) (z + h), (double) this.zLevel, (double) ((float) (u + w) * f),
                (double) ((float) (v + h) * f1));
        tessellator.addVertexWithUV((double) (x + w), (double) (z + 0), (double) this.zLevel, (double) ((float) (u + w) * f),
                (double) ((float) (v + 0) * f1));
        tessellator.addVertexWithUV((double) (x + 0), (double) (z + 0), (double) this.zLevel, (double) ((float) (u + 0) * f),
                (double) ((float) (v + 0) * f1));
        tessellator.draw();
    }
    
    @SuppressWarnings("cast")
    public void drawTexturedModalRect3(int x, int z, int u, int v, int w, int h) {
    
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x + 0), (double) (z + h), (double) this.zLevel, (double) ((float) (u + 0) * f),
                (double) ((float) (v + h) * f1));
        tessellator.addVertexWithUV((double) (x + w), (double) (z + h), (double) this.zLevel, (double) ((float) (u + w) * f),
                (double) ((float) (v + h) * f1));
        tessellator.addVertexWithUV((double) (x + w), (double) (z + 0), (double) this.zLevel, (double) ((float) (u + w) * f),
                (double) ((float) (v + 0) * f1));
        tessellator.addVertexWithUV((double) (x + 0), (double) (z + 0), (double) this.zLevel, (double) ((float) (u + 0) * f),
                (double) ((float) (v + 0) * f1));
        tessellator.draw();
    }
}
