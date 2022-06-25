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
 *     @author Lumien
 */

package com.bluepowermod.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import com.bluepowermod.container.ContainerCanvasBag;
import com.bluepowermod.reference.Refs;
import net.minecraft.network.chat.Component;

public class GuiCanvasBag extends GuiContainerBaseBP<ContainerCanvasBag> implements MenuAccess<ContainerCanvasBag> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/canvas_bag.png");

    public GuiCanvasBag(ContainerCanvasBag container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, I18n.get("item.bluepower." + title.getContents()), 8.0F, 6.0F, COLOR_TEXT);
    }
}
