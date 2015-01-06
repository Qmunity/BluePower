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

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiAlloyFurnace extends GuiBase {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/alloy_furnace.png");
    private final TileAlloyFurnace        furnace;
    
    public GuiAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace furnace) {
    
        super(furnace, new ContainerAlloyFurnace(invPlayer, furnace), resLoc);
        this.furnace = furnace;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        int burningPercentage = (int) (furnace.getBurningPercentage() * 14);
        drawTexturedModalRect(x + 22, y + 54 + 14 - burningPercentage, 177, 14 - burningPercentage, 14, burningPercentage + 0);
        
        int processPercentage = (int) (furnace.getProcessPercentage() * 22);
        drawTexturedModalRect(x + 103, y + 35, 178, 14, processPercentage, 15);
    }
    
}
