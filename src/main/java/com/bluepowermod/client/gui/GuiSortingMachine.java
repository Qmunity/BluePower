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

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetColor;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.containers.ContainerSortingMachine;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;
import com.bluepowermod.tileentities.tier2.TileSortingMachine.PullMode;
import com.bluepowermod.tileentities.tier2.TileSortingMachine.SortMode;
import com.bluepowermod.util.Refs;

/**
 * 
 * @author MineMaarten
 */

public class GuiSortingMachine extends GuiBase {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/sorting_machine.png");
    private final TileSortingMachine      sortingMachine;
    
    public GuiSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {
    
        super(sortingMachine, new ContainerSortingMachine(invPlayer, sortingMachine), resLoc);
        this.sortingMachine = sortingMachine;
        ySize = 222;
    }
    
    @Override
    public void initGui() {
    
        super.initGui();
        for (int i = 0; i < 8; i++) {
            WidgetColor colorWidget = new WidgetColor(i, guiLeft + 27 + 18 * i, guiTop + 110);
            colorWidget.value = sortingMachine.colors[i].ordinal();
            addWidget(colorWidget);
        }
        
        switch (sortingMachine.sortMode) {
            case ANY_ITEM_DEFAULT:
            case ANY_STACK_DEFAULT:
                WidgetColor colorWidget = new WidgetColor(8, guiLeft + 7, guiTop + 122);
                colorWidget.value = sortingMachine.colors[8].ordinal();
                addWidget(colorWidget);
        }
        
        WidgetMode pullModeWidget = new WidgetMode(9, guiLeft + 7, guiTop + 90, 196, PullMode.values().length, Refs.MODID + ":textures/gui/sorting_machine.png") {
            
            @Override
            public void addTooltip(List<String> curTip, boolean shiftPressed) {
            
                curTip.add("gui.pullMode");
                curTip.add(PullMode.values()[value].toString());
                if (shiftPressed) {
                    curTip.add(PullMode.values()[value].toString() + ".info");
                } else {
                    curTip.add("gui.sneakForInfo");
                }
            }
        };
        pullModeWidget.value = sortingMachine.pullMode.ordinal();
        addWidget(pullModeWidget);
        
        WidgetMode sortModeWidget = new WidgetMode(10, guiLeft + 7, guiTop + 106, 210, TileSortingMachine.SortMode.values().length, Refs.MODID + ":textures/gui/sorting_machine.png") {
            
            @Override
            public void addTooltip(List<String> curTip, boolean shiftPressed) {
            
                curTip.add("gui.sortMode");
                curTip.add(TileSortingMachine.SortMode.values()[value].toString());
                if (shiftPressed) {
                    curTip.add(TileSortingMachine.SortMode.values()[value].toString() + ".info");
                } else {
                    curTip.add("gui.sneakForInfo");
                }
            }
        };
        sortModeWidget.value = sortingMachine.sortMode.ordinal();
        addWidget(sortModeWidget);
    }
    
    @Override
    public void actionPerformed(IGuiWidget widget) {
    
        BaseWidget baseWidget = (BaseWidget) widget;
        NetworkHandler.sendToServer(new MessageGuiUpdate(sortingMachine, widget.getID(), baseWidget.value));
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        if (sortingMachine.sortMode == SortMode.ALLSTACK_SEQUENTIAL || sortingMachine.sortMode == SortMode.ANYSTACK_SEQUENTIAL) Gui.func_146110_a(guiLeft + 24 + sortingMachine.curColumn * 18, guiTop + 16, 176, 0, 20, 92, 256, 256);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 25, ySize - 94 + 2, COLOR_TEXT);
        drawHorizontalAlignedString(7, 5, xSize - 14, I18n.format("tile." + Refs.MODID + ":" + Refs.SORTING_MACHINE_NAME + ".name"), false);
    }
    
}
