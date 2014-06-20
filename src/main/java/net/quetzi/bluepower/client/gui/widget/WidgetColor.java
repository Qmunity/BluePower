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
package net.quetzi.bluepower.client.gui.widget;

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemDye;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

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
            if (++value > 16) value = 0;
        } else if (button == 1) {
            if (--value < 0) value = 16;
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void render(int mouseX, int mouseY) {
    
        super.render(mouseX, mouseY);
        if (value < 16) {
            Gui.drawRect(x + 5, y + 5, x + 9, y + 9, 0xFF000000 + ItemDye.field_150922_c[value]);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    
    @Override
    public void addTooltip(List<String> curTooltip) {
    
        curTooltip.add("Paint item:");
        if (value < 16) {
            curTooltip.add(ItemDye.field_150923_a[value]);
        } else {
            curTooltip.add("None");
        }
    }
    
}
