/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.container.ContainerMonitor;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileMonitor;

public class GuiMonitor extends GuiContainerBaseBP<ContainerMonitor> implements MenuAccess<ContainerMonitor> {

    private static final ResourceLocation resLoc = ResourceLocation.parse(Refs.MODID + ":textures/gui/monitorgui.png");
    private static final ResourceLocation chracterSetResLoc = ResourceLocation.parse(Refs.MODID + ":textures/gui/65el02_chars.png");
    private final ContainerMonitor monitor;

    public GuiMonitor(ContainerMonitor container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
        this.monitor = container;
        imageWidth = 350;
        imageHeight = 230;
        width = 350 / 2;
        // TODO: fix height and width fields as well
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindForSetup(resLoc);
        int k = (width - imageWidth) / 2;
        int l = (height - imageHeight) / 2;
        drawTexturedModalRect2(k, l, 0, 0, imageWidth, imageHeight);

        // screen color
        this.minecraft.getTextureManager().bindForSetup(chracterSetResLoc);
        GL11.glColor4f(TileMonitor.screenColor[0], TileMonitor.screenColor[1], TileMonitor.screenColor[2], 1.0F);

        for (int row = 0; row < 50; row++) {
            for (int col = 0; col < 80; col++) {
                //byte character = monitor.screenMemory[row * 80 + col];
                // TODO: overlay cursor character
                //if (character != 32) {
                    //drawCharacter(row, col, character);
                //}
            }
        }
    }



    private void drawCharacter(int row, int col, byte character) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int tempOffset = 0; // 350;
        if (TileMonitor.mode80x40) {
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
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(x + 0, z + h, 0).setUv((u + 0) * f, (v + h) * f1);
        buffer.addVertex(x + w, z + h, 0).setUv((u + w) * f, (v + h) * f1);
        buffer.addVertex(x + w, z + 0, 0).setUv((u + w) * f, (v + 0) * f1);
        buffer.addVertex(x + 0, z + 0, 0).setUv((u + 0) * f, (v + 0) * f1);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    @SuppressWarnings("cast")
    public void drawTexturedModalRect3(int x, int z, int u, int v, int w, int h) {

        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(x + 0, z + h, 0).setUv((u + 0) * f, (v + h) * f1);
        buffer.addVertex(x + w, z + h, 0).setUv((u + w) * f, (v + h) * f1);
        buffer.addVertex(x + w, z + 0, 0).setUv((u + w) * f, (v + 0) * f1);
        buffer.addVertex(x + 0, z + 0, 0).setUv((u + 0) * f, (v + 0) * f1);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }
}
