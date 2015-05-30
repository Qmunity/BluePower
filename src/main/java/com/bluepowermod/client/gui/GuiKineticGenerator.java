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
 *     @author Quetzi
 */

package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.container.ContainerKineticGenerator;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileKineticGenerator;

public class GuiKineticGenerator extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/kinetic_generator.png");
    private final TileKineticGenerator kineticGenerator;

    public GuiKineticGenerator(InventoryPlayer invPlayer, TileKineticGenerator kineticGenerator) {

        super(kineticGenerator, new ContainerKineticGenerator(invPlayer, kineticGenerator), resLoc);
        this.kineticGenerator = kineticGenerator;
        ySize = 165;
    }

    protected int getPowerBarXPos() {
        return 6;
    }

    protected int getPowerBarYPos() {
        return 37;
    }

}
