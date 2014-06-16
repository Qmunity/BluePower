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
import net.quetzi.bluepower.containers.ContainerSortingMachine;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine;

public class GuiSortingMachine extends GuiBase {
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/GUI/sorting_machine.png");
    private final TileSortingMachine      sortingMachine;
    
    public GuiSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {
    
        super(new ContainerSortingMachine(invPlayer, sortingMachine), resLoc);
        this.sortingMachine = sortingMachine;
        ySize = 222;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        // fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
        drawHorizontalAlignedString(7, 3, xSize - 14, sortingMachine.getInventoryName(), true);
        
        checkTooltips(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
    }
    
}
