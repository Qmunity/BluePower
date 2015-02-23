/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.widget;

import java.util.List;

import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.bluepowermod.util.Refs;

public class WidgetFuzzySetting extends WidgetMode {

    public WidgetFuzzySetting(int id, int x, int y) {

        super(id, x, y, 0, 3, new String[] { Refs.MODID + ":textures/gui/widgets/fuzzy_widget_normal.png",
                Refs.MODID + ":textures/gui/widgets/fuzzy_widget_fuzzy.png", Refs.MODID + ":textures/gui/widgets/fuzzy_widget_exact.png" });

    }

    @Override
    public void render(int mouseX, int mouseY, float frame) {

        textureIndex = value;
        super.render(mouseX, mouseY, frame);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

        curTip.add("gui.bluepower:widget.fuzzy");
        switch (value) {
        case 0:
            curTip.add("gui.bluepower:widget.fuzzy.normal");
            if (shiftPressed)
                curTip.add("gui.bluepower:widget.fuzzy.normal.info");
            break;
        case 1:
            curTip.add("gui.bluepower:widget.fuzzy.fuzzy");
            if (shiftPressed)
                curTip.add("gui.bluepower:widget.fuzzy.fuzzy.info");
            break;
        case 2:
            curTip.add("gui.bluepower:widget.fuzzy.exact");
            if (shiftPressed)
                curTip.add("gui.bluepower:widget.fuzzy.exact.info");
            break;
        }
        if (!shiftPressed)
            curTip.add("gui.bluepower:tooltip.sneakForInfo");
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
