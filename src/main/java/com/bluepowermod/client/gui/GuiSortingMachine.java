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

package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.*;
import com.bluepowermod.container.ContainerSortingMachine;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import com.bluepowermod.tile.tier2.TileSortingMachine.PullMode;
import com.bluepowermod.tile.tier2.TileSortingMachine.SortMode;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * @author MineMaarten
 */

public class GuiSortingMachine extends GuiContainerBaseBP<ContainerSortingMachine> implements MenuAccess<ContainerSortingMachine> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/sorting_machine.png");
    private final ContainerSortingMachine sortingMachine;

    public GuiSortingMachine(ContainerSortingMachine container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
        this.sortingMachine = container;
        imageHeight = 239;
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 8; i++) {
            WidgetColor colorWidget = new WidgetColor(i, leftPos + 27 + 18 * i, topPos + 110);
            colorWidget.value = i;
            addWidget(colorWidget);
        }

        switch (sortingMachine.sortMode) {
        case ANY_ITEM_DEFAULT:
        case ANY_STACK_DEFAULT:
            WidgetColor colorWidget = new WidgetColor(8, leftPos + 7, topPos + 122);
            colorWidget.value = 8;
            addWidget(colorWidget);
        }

        WidgetMode pullModeWidget = new WidgetMode(9, leftPos + 7, topPos + 90, 196, PullMode.values().length, Refs.MODID
                + ":textures/gui/sorting_machine.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.pullMode");
                curTip.add(PullMode.values()[value].toString());
                if (shiftPressed) {
                    curTip.add(PullMode.values()[value].toString() + ".info");
                } else {
                    curTip.add("gui.bluepower:tooltip.sneakForInfo");
                }
            }
        };
        pullModeWidget.value = sortingMachine.pullMode.ordinal();
        addWidget(pullModeWidget);

        WidgetMode sortModeWidget = new WidgetMode(10, leftPos + 7, topPos + 106, 210, TileSortingMachine.SortMode.values().length,
                Refs.MODID + ":textures/gui/sorting_machine.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.bluepower:sortingMachine.sortMode");
                curTip.add(TileSortingMachine.SortMode.values()[value].toString());
                if (shiftPressed) {
                    curTip.add(TileSortingMachine.SortMode.values()[value].toString() + ".info");
                } else {
                    curTip.add("gui.bluepower:tooltip.sneakForInfo");
                }
            }
        };
        sortModeWidget.value = sortingMachine.sortMode.ordinal();
        addWidget(sortModeWidget);

        for (int i = 0; i < 8; i++) {
            WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(i + 11, leftPos + 27 + 18 * i, topPos + 126);
            fuzzyWidget.value = sortingMachine.fuzzySettings[i];
            addWidget(fuzzyWidget);
        }
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        //TODO: Check Network Handler
        //BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(sortingMachine, widget.getID(), baseWidget.value));
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float f, int i, int j) {
        super.renderBg(matrixStack, f, i, j);

        if (sortingMachine.sortMode == SortMode.ALLSTACK_SEQUENTIAL || sortingMachine.sortMode == SortMode.ANYSTACK_SEQUENTIAL) {
            this.minecraft.getTextureManager().bind(resLoc);
            AbstractGui.blit(matrixStack, leftPos + 24 + sortingMachine.curColumn * 18, topPos + 16, 176, 0, 20, 92, 256, 256);
        }
    }

}
