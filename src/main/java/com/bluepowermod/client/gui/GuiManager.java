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
import uk.co.qmunity.lib.client.gui.widget.BaseWidget;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.bluepowermod.client.gui.widget.WidgetColor;
import com.bluepowermod.client.gui.widget.WidgetFuzzySetting;
import com.bluepowermod.client.gui.widget.WidgetNumber;
import com.bluepowermod.container.ContainerManager;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileManager;

/**
 * @author MineMaarten
 */
public class GuiManager extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/manager.png");
    protected TileManager manager;

    public GuiManager(InventoryPlayer invPlayer, TileManager manager) {

        super(manager, new ContainerManager(invPlayer, manager), resLoc);
        this.manager = manager;
        ySize = 187;
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetColor colorWidget = new WidgetColor(0, guiLeft + 155, guiTop + 55);
        colorWidget.value = manager.filterColor.ordinal();
        addWidget(colorWidget);

        WidgetMode modeWidget = new WidgetMode(1, guiLeft + 155, guiTop + 21, 176, 2, Refs.MODID + ":textures/gui/manager.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.mode");
                String mode = null;
                switch (value) {
                case 0:
                    mode = "gui.bluepower:manager.mode.exact";
                    break;
                case 1:
                    mode = "gui.bluepower:manager.mode.all";
                    break;
                }
                curTip.add(mode);
                if (shiftPressed) {
                    curTip.add(mode + ".info");
                } else {
                    curTip.add("gui.bluepower:tooltip.sneakForInfo");
                }
            }
        };
        modeWidget.value = manager.mode;
        addWidget(modeWidget);

        WidgetNumber numberWidget = new WidgetNumber(2, guiLeft + 155, guiTop + 38, 9) {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.priority");
                curTip.add("gui.bluepower:sortingMachine.priority.info");
            }
        };
        numberWidget.value = manager.priority;
        addWidget(numberWidget);

        WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(3, guiLeft + 155, guiTop + 72);
        fuzzyWidget.value = manager.fuzzySetting;
        addWidget(fuzzyWidget);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(manager, widget.getID(), baseWidget.value));
    }

    @Override
    protected int getPowerBarYPos() {
        return 20;
    }
}
