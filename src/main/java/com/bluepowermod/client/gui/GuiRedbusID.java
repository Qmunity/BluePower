/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.bluepowermod.container.ContainerRedbusID;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.IRedBusWindow;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiRedbusID extends GuiContainerBaseBP<ContainerRedbusID> implements IHasContainer<ContainerRedbusID> {

    private final ContainerRedbusID device;

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/redbusgui.png");

    public GuiRedbusID(ContainerRedbusID container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title, resLoc);
        this.device = container;

        xSize = 123;
        ySize = 81;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {

        drawHorizontalAlignedString(matrixStack, 7, 4, xSize - 14,  "gui.redbusgui", true);

        drawHorizontalAlignedString(matrixStack, 7, 60, xSize - 14, "gui.redbus.id" + ":" + IRedBusWindow.redbus_id,
                true);
    }

    // TODO: clicking on switches toggles state and updates redbus_id
}
