/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.qmunity.lib.client.gui.widget.BaseWidget;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.bluepowermod.client.gui.widget.WidgetFuzzySetting;
import com.bluepowermod.container.ContainerItemDetector;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.tile.tier1.TileItemDetector;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiItemDetector extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/item_detector.png");
    private final TileItemDetector itemDetector;

    public GuiItemDetector(InventoryPlayer invPlayer, TileItemDetector itemDetector) {

        super(itemDetector, new ContainerItemDetector(invPlayer, itemDetector), resLoc);
        this.itemDetector = itemDetector;
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetMode modeWidget = new WidgetMode(0, guiLeft + 152, guiTop + 10, 176, 3, Refs.MODID + ":textures/gui/item_detector.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.mode");
                String mode;
                switch (value) {
                case 0:
                    mode = "gui.bluepower:itemDetector.mode.item";
                    break;
                case 1:
                    mode = "gui.bluepower:itemDetector.mode.stack";
                    break;
                default:
                    mode = "gui.bluepower:itemDetector.mode.stuffed";

                }
                curTip.add(mode);
                if (shiftPressed) {
                    curTip.add(mode + ".info");
                } else {
                    curTip.add("gui.bluepower:tooltip.sneakForInfo");
                }
            }
        };
        modeWidget.value = itemDetector.mode;
        addWidget(modeWidget);

        WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(1, guiLeft + 152, guiTop + 55);
        fuzzyWidget.value = itemDetector.fuzzySetting;
        addWidget(fuzzyWidget);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(itemDetector, widget.getID(), baseWidget.value));
    }
}
