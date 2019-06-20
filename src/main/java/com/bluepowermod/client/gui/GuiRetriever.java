/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerRetriever;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileRetriever;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author MineMaarten
 */
public class GuiRetriever extends GuiContainerBaseBP<ContainerRetriever> implements IHasContainer<ContainerRetriever> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/retriever.png");
    private final ContainerRetriever filter;
    public GuiRetriever(ContainerRetriever container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title, resLoc);
        this.filter = container;
    }

    @Override
    public void init() {

        super.init();
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
        colorWidget.value = filter.mode;
        addWidget(colorWidget);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        super.drawGuiContainerBackgroundLayer(f, i, j);

        if (filter.mode == 0) {
            int curSlot = filter.slotIndex;
            AbstractGui.blit(guiLeft + 60 + curSlot % 3 * 18, guiTop + 15 + 18 * (curSlot / 3), 182, 0, 20, 20, 256, 256);
        }
    }

}
