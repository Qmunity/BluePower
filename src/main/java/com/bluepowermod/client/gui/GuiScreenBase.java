/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import ResourceLocation;

/**
 *
 * @author MineMaarten
 */

@OnlyIn(Dist.CLIENT)
public abstract class GuiScreenBase extends Screen {

    protected int guiLeft, guiTop, xSize, ySize;

    public GuiScreenBase(int xSize, int ySize, Component name) {
        super(name);

        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void init() {

        super.init();
        guiLeft = width / 2 - xSize / 2;
        guiTop = height / 2 - ySize / 2;
    }

    protected abstract ResourceLocation getTexture();

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        if (getTexture() != null) {
            renderBackground(matrixStack);
            this.minecraft.getTextureManager().bind(getTexture());
            blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        }
        super.render(matrixStack, x, y, partialTicks);
    }
}
