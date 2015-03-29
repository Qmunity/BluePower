/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.bluepowermod.container.ContainerRedbusID;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.IRedBusWindow;

public class GuiRedbusID extends GuiContainerBaseBP {

    private final IRedBusWindow device;

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/redbusgui.png");

    public GuiRedbusID(InventoryPlayer invPlayer, IRedBusWindow device) {

        super(new ContainerRedbusID(invPlayer, device), resLoc);
        this.device = device;

        xSize = 123;
        ySize = 81;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        drawHorizontalAlignedString(7, 4, xSize - 14, StatCollector.translateToLocal("gui.redbusgui"), true);

        drawHorizontalAlignedString(7, 60, xSize - 14, StatCollector.translateToLocal("gui.redbus.id") + ":" + IRedBusWindow.redbus_id,
                true);
    }

    // TODO: clicking on switches toggles state and updates redbus_id
}
