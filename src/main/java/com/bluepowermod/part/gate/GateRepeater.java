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

import com.bluepowermod.client.renderers.RenderHelper;

import java.util.List;

/**
 * Created by Quetzi on 04/11/14.
 */
public class GateRepeater extends GateBase {

    private boolean power = false;
    private boolean powerBack = false;
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
    public void renderTop(float frame) {

        renderTop("front", power);
        RenderHelper.renderRedstoneTorch(-3D / 16D, 1D / 8D, -6D /16D, 1D / 2D, !power);

        renderTop("back", powerBack);
        RenderHelper.renderRedstoneTorch(1D / 4D, 1D / 8D, 1D / 16D * (4 - location), 1D / 2D, (back().getInput() > 0));
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (powerBack != back().getInput() > 0 && ticksRemaining == 0) {
            ticksRemaining = ticks[location];
        }
        powerBack = back().getInput() > 0;
        if (ticksRemaining == 0) {
            power = powerBack;
        } else {
            ticksRemaining--;
        }

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
    public void addWailaInfo(List<String> info) {

        /*
         * info.add(Color.YELLOW + I18n.format("gui.connections") + ":"); info.add("  " + FaceDirection.LEFT.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.LEFT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled"))); info.add("  " + FaceDirection.BACK.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.BACK).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled"))); info.add("  " + FaceDirection.RIGHT.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.RIGHT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled")));
         */
    }
}
