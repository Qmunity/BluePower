/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.bluepowermod.container.ContainerRetriever;
import com.bluepowermod.tile.tier2.TileRetriever;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiRetriever extends GuiFilter {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/retriever.png");

    public GuiRetriever(InventoryPlayer invPlayer, TileRetriever retriever) {

        super(new ContainerRetriever(invPlayer, retriever), retriever, resLoc);
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetMode colorWidget = new WidgetMode(2, guiLeft + 117, guiTop + 20, 202, 2, Refs.MODID + ":textures/gui/retriever.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.mode");
                curTip.add("gui.bluepower:retriever.mode." + (value == 0 ? "sequential" : "any"));
                if (shiftPressed) {
                    curTip.add("gui.bluepower:retriever.mode." + (value == 0 ? "sequential" : "any") + ".info");
                } else {
                    curTip.add("gui.bluepower:tooltip.sneakForInfo");
                }
            }
        };
        colorWidget.value = ((TileRetriever) filter).mode;
        addWidget(colorWidget);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        super.drawGuiContainerBackgroundLayer(f, i, j);

        if (((TileRetriever) filter).mode == 0) {
            int curSlot = ((TileRetriever) filter).slotIndex;
            Gui.func_146110_a(guiLeft + 60 + curSlot % 3 * 18, guiTop + 15 + 18 * (curSlot / 3), 182, 0, 20, 20, 256, 256);
        }
    }

}
