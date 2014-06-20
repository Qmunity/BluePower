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

package net.quetzi.bluepower.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.client.gui.widget.BaseWidget;
import net.quetzi.bluepower.client.gui.widget.IGuiWidget;
import net.quetzi.bluepower.client.gui.widget.WidgetColor;
import net.quetzi.bluepower.client.gui.widget.WidgetMode;
import net.quetzi.bluepower.containers.ContainerSortingMachine;
import net.quetzi.bluepower.network.NetworkHandler;
import net.quetzi.bluepower.network.messages.MessageGuiUpdate;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine.PullMode;

/**
 * 
 * @author MineMaarten
 */

public class GuiSortingMachine extends GuiBase {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/GUI/sorting_machine.png");
    private final TileSortingMachine      sortingMachine;
    
    public GuiSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {
    
        super(new ContainerSortingMachine(invPlayer, sortingMachine), resLoc);
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
        
        WidgetMode pullModeWidget = new WidgetMode(9, guiLeft + 7, guiTop + 90, 196, PullMode.values().length, Refs.MODID + ":textures/GUI/sorting_machine.png") {
            
            @Override
            public void addTooltip(List<String> curTip) {
            
                curTip.add("gui.pullMode");
                curTip.add(PullMode.values()[value].toString());
            }
        };
        pullModeWidget.value = sortingMachine.pullMode.ordinal();
        addWidget(pullModeWidget);
        
        WidgetMode sortModeWidget = new WidgetMode(10, guiLeft + 7, guiTop + 106, 210, TileSortingMachine.SortMode.values().length, Refs.MODID + ":textures/GUI/sorting_machine.png") {
            
            @Override
            public void addTooltip(List<String> curTip) {
            
                curTip.add("gui.sortMode");
                curTip.add(TileSortingMachine.SortMode.values()[value].toString());
            }
        };
        sortModeWidget.value = sortingMachine.sortMode.ordinal();
        addWidget(sortModeWidget);
    }
    
    @Override
    public void actionPerformed(IGuiWidget widget) {
    
        NetworkHandler.sendToServer(new MessageGuiUpdate(sortingMachine, widget.getID(), ((BaseWidget) widget).value));
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        // fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
        drawHorizontalAlignedString(7, 3, xSize - 14, sortingMachine.getInventoryName(), true);
        
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
    }
    
}
