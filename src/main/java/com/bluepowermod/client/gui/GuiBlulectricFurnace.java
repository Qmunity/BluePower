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

import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricFurnace;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author MineMaarten
 */
public class GuiBlulectricFurnace extends GuiContainerBaseBP<ContainerBlulectricFurnace> implements IHasContainer<ContainerBlulectricFurnace> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/powered_furnace.png");
    private final ContainerBlulectricFurnace furnace;

    public GuiBlulectricFurnace(ContainerBlulectricFurnace container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title, resLoc);
        this.furnace = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        int processPercentage = (furnace.getCookProgressionScaled() * 22);
        this.blit(x + 103, y + 35, 178, 14, processPercentage, 15);
    }

}
