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

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.render.RenderHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GatePulseFormer extends GateBase {

    private final boolean power[] = new boolean[4];

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().disable();
        back().enable();
        right().disable();
    }

    @Override
    public String getId() {

        return "pulseformer";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderTop(float frame) {

        RenderHelper.renderDigitalRedstoneTorch(3 / 16D, 1D / 8D, -1 / 16D, 9D / 16D, !power[0]);
        RenderHelper.renderDigitalRedstoneTorch(-3 / 16D, 1D / 8D, -1 / 16D, 9D / 16D, power[2]);
        RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, 5 / 16D, 9D / 16D, !power[2] && power[1]);

        renderTop("center", !power[1]);
        renderTop("back", power[0]);
        renderTop("left", !power[1]);
        renderTop("right", power[2]);
    }

    @Override
    public void doLogic() {

        power[0] = back().getInput() > 0;
    }

    @Override
    public void tick() {

        power[3] = power[2];
        power[2] = power[1];
        power[1] = power[0];
        front().setOutput(!power[2] && power[1]);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("back", power[0]);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        power[0] = tag.getBoolean("back");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

    }

}
