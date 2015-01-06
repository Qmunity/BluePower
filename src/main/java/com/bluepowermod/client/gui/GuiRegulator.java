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
 *
 *     @author Quetzi
 */

package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetColor;
import com.bluepowermod.client.gui.widget.WidgetFuzzySetting;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.container.ContainerRegulator;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.tile.tier2.TileRegulator;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiRegulator extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/regulator.png");
    protected TileRegulator regulator;

    public GuiRegulator(InventoryPlayer invPlayer, TileRegulator regulator) {

        super(regulator, new ContainerRegulator(invPlayer, regulator), resLoc);
        this.regulator = regulator;
        xSize = 212;
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetColor colorWidget = new WidgetColor(0, guiLeft + 135, guiTop + 55);
        colorWidget.value = regulator.color.ordinal();
        addWidget(colorWidget);

        WidgetMode modeWidget = new WidgetMode(1, guiLeft + 135, guiTop + 20, 216, 2, Refs.MODID + ":textures/gui/regulator.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.mode");
                curTip.add("gui.regulator.mode." + (value == 0 ? "ratio" : "supply"));
                if (shiftPressed) {
                    curTip.add("gui.regulator.mode." + (value == 0 ? "ratio" : "supply") + ".info");
                } else {
                    curTip.add("gui.sneakForInfo");
                }
            }
        };
        modeWidget.value = regulator.mode;
        addWidget(modeWidget);

        WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(2, guiLeft + 135, guiTop + 70);
        fuzzyWidget.value = regulator.fuzzySetting;
        addWidget(fuzzyWidget);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        NetworkHandler.sendToServer(new MessageGuiUpdate(regulator, widget.getID(), baseWidget.value));
    }
}
