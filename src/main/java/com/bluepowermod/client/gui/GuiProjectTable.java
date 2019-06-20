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

import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.container.ContainerAlloyFurnace;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.util.text.ITextComponent;

/**
 * @author MineMaarten
 */
public class GuiProjectTable extends GuiContainerBaseBP<ContainerProjectTable> implements IHasContainer<ContainerProjectTable> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/project_table.png");
    private final ContainerProjectTable projectTable;

    public GuiProjectTable(ContainerProjectTable container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title, resLoc);
        this.projectTable = container;
        ySize = 208;
    }

    @Override
    public void init() {

        super.init();
        addWidget(new WidgetMode(0, guiLeft + 15, guiTop + 20, 176, 1, Refs.MODID + ":textures/gui/project_table.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shift) {

                curTip.add("gui.bluepower:projectTable.clearGrid");
            }
        });
    }

    @Override
    public void actionPerformed(IGuiWidget button) {

        //BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(projectTable, 0, 0));
    }
}
