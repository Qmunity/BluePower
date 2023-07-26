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
package com.bluepowermod.client.gui.widget;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author MineMaarten
 */
public class WidgetColor extends BaseWidget {

    public WidgetColor(int id, int x, int y) {

        super(id, x, y, 14, 14, Refs.MODID + ":textures/gui/widgets/color_widget.png");
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        if (button == 0) {
            if (++value > 16)
                value = 0;
        } else if (button == 1) {
            if (--value < 0)
                value = 16;
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float frame) {

        super.render(guiGraphics, mouseX, mouseY, frame);
        if (value < 16) {
            guiGraphics.fill(x + 5, y + 5, x + 9, y + 9, 0xFF000000 + MinecraftColor.values()[value].getHex());
            GL11.glColor4d(1, 1, 1, 1);
        }
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTooltip, boolean shiftPressed) {

        if (value < 16) {
            curTooltip.add("bluepower:color." + MinecraftColor.values()[value].name());
        } else {
            curTooltip.add("bluepower:color.none");
        }
    }

}
