/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.widget;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.qmunity.lib.client.gui.widget.BaseWidget;

public class WidgetTab extends BaseWidget {

    private final int singleTabHeight;
    private final int tabAmount;
    public boolean[] enabledTabs;

    public WidgetTab(int id, int x, int y, int width, int height, int textureU, int tabAmount, String textureLoc) {

        super(id, x, y, width, height * tabAmount, textureU, 0, textureLoc);
        singleTabHeight = height;
        this.tabAmount = tabAmount;
        enabledTabs = new boolean[tabAmount];
        Arrays.fill(enabledTabs, true);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        int clickedTab = (mouseY - y) / singleTabHeight;
        if (enabledTabs[clickedTab]) {
            value = clickedTab;
            super.onMouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {

        if (textures.length > 0)
            Minecraft.getMinecraft().getTextureManager().bindTexture(textures[0]);

        for (int i = 0; i < tabAmount; i++) {
            if (i == value) {
                GL11.glColor4d(1, 1, 1, 1);
            } else {
                if (enabledTabs[i]) {
                    GL11.glColor4d(0.6, 0.6, 0.6, 1);
                } else {
                    GL11.glColor4d(0.2, 0.2, 0.2, 1);
                }
            }
            Gui.func_146110_a(x, y + singleTabHeight * i, getTextureU(), getTextureV() + singleTabHeight * i, width, singleTabHeight, 256, 256);
        }
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

        addTooltip((mouseY - y) / singleTabHeight, curTip, shiftPressed);
    }

    protected void addTooltip(int hoveredTab, List<String> curTip, boolean shiftPressed) {

    }
}
