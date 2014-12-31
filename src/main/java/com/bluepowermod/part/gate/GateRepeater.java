/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.util.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Quetzi on 04/11/14.
 */
public class GateRepeater extends GateBase {

    private boolean power = false;
    private boolean lastInput = false;
    private boolean currentUpdate = false;

    private int ticksRemaining = 0;
    private int location = 0;
    private int[] ticks = { 1, 2, 3, 4, 8, 16, 32, 64, 128, 256, 1024 };

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        back().enable();
    }

    @Override
    public String getId() {

        return "repeater";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderTop(float frame) {

        renderTop("front", !power);
        RenderHelper.renderDigitalRedstoneTorch(3D / 16D, 5D / 16D, 6D / 16D, 1D / 2D, power);

        renderTop("back", currentUpdate);
        RenderHelper.renderDigitalRedstoneTorch(-1D / 4D, 4D / 16D, -(1D / 16D * (5 - location)), 1D / 2D, !power);
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        boolean in = back().getInput() > 0;

        if (in != currentUpdate) {
            if (in || (!in && ticksRemaining == 0)) {
                ticksRemaining = ticks[location];
                currentUpdate = in;
            }
        }

        if (ticksRemaining > 0)
            ticksRemaining--;

        if (ticksRemaining == 0)
            power = currentUpdate;

        front().setOutput(power ? 15 : 0);
    }

    @Override
    protected boolean changeMode() {

        location++;
        if (location == 9) {
            location = 0;
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        info.add(Color.YELLOW + I18n.format("gui.connections") + ":");
        info.add("  "
                + Dir.LEFT.getLocalizedName()
                + ": "
                + (getConnection(Dir.LEFT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
        info.add("  "
                + Dir.BACK.getLocalizedName()
                + ": "
                + (getConnection(Dir.BACK).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
        info.add("  "
                + Dir.RIGHT.getLocalizedName()
                + ": "
                + (getConnection(Dir.RIGHT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item == null) {
            location = (location + 1) % ticks.length;
            return true;
        }

        return super.onActivated(player, hit, item);
    }
}
