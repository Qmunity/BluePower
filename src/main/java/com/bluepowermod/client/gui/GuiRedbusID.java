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
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GuiRedbusID extends GuiContainerBaseBP<ContainerRedbusID> implements MenuAccess<ContainerRedbusID> {

    private final ContainerRedbusID device;

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/redbusgui.png");

    public GuiRedbusID(ContainerRedbusID container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
        this.device = container;

        imageWidth = 123;
        imageHeight = 81;
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {

        drawHorizontalAlignedString(matrixStack, 7, 4, imageWidth - 14,  "gui.redbusgui", true);

        drawHorizontalAlignedString(matrixStack, 7, 60, imageWidth - 14, "gui.redbus.id" + ":" + IRedBusWindow.redbus_id,
                true);
    }

    // TODO: clicking on switches toggles state and updates redbus_id
}
