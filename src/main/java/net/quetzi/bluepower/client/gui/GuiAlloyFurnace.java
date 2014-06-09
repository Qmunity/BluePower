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

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerAlloyFurnace;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

public class GuiAlloyFurnace extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/GUI/alloy_furnace.png");
    private final TileAlloyFurnace furnace;

    public GuiAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace _furnace) {

        super(new ContainerAlloyFurnace(invPlayer, _furnace), resLoc);
        furnace = _furnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        // fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
        drawHorizontalAlignedString(7, 3, xSize - 14, furnace.getInventoryName(), true);

        checkTooltips(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        super.drawGuiContainerBackgroundLayer(f, i, j);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        int burningPercentage = (int) (furnace.getBurningPercentage() * 14);
        // Todo: Tweak these variables a bit till it lines up perfectly.
        drawTexturedModalRect(x + 22, y + 54 + 14 - burningPercentage, 177, 14 - burningPercentage, 14, burningPercentage + 0);

        int processPercentage = (int) (furnace.getProcessPercentage() * 22);
        drawTexturedModalRect(x + 103, y + 35, 178, 14, processPercentage, 15);
    }

}
