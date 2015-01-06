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

import com.bluepowermod.container.ContainerDiskDrive;
import com.bluepowermod.tile.tier3.TileDiskDrive;
import com.bluepowermod.util.Refs;

public class GuiDiskDrive extends GuiBase {
    
    private final TileDiskDrive           diskDrive;
    
    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/diskdrivegui.png");
    
    public GuiDiskDrive(InventoryPlayer invPlayer, TileDiskDrive diskDrive) {
    
        super(new ContainerDiskDrive(invPlayer, diskDrive), resLoc);
        this.diskDrive = diskDrive;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    
    }
}
