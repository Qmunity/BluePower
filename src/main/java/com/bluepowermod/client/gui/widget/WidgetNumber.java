/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.widget;

import uk.co.qmunity.lib.client.gui.widget.WidgetMode;
import net.minecraft.client.Minecraft;

import com.bluepowermod.util.Refs;

public class WidgetNumber extends WidgetMode {

    public WidgetNumber(int id, int x, int y, int maxNumber) {

        super(id, x, y, 0, maxNumber, Refs.MODID + ":textures/gui/widgets/empty_widget.png");
    }

    @Override
    public void render(int mouseX, int mouseY, float frame) {

        super.render(mouseX, mouseY, frame);
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
