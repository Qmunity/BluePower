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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.reference.Refs;
import net.minecraft.network.chat.Component;

/**
 * @author MineMaarten
 */
public class GuiAlloyFurnace extends GuiContainerBaseBP<ContainerAlloyFurnace> implements MenuAccess<ContainerAlloyFurnace> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/alloy_furnace.png");
    private final ContainerAlloyFurnace furnace;

    public GuiAlloyFurnace(ContainerAlloyFurnace container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
        this.furnace = container;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY){

        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int burningPercentage = (int)(furnace.getBurningPercentage() * 13);
        if (burningPercentage > 0)
            guiGraphics.blit(resLoc, x + 22, y + 54 + 13 - burningPercentage, 177, 13 - burningPercentage, 14, burningPercentage + 1);

        int processPercentage = (int)(furnace.getProcessPercentage() * 22);
        guiGraphics.blit(resLoc, x + 103, y + 35, 178, 14, processPercentage, 15);
    }

}
