/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import com.bluepowermod.client.renderers.RenderHelper;

public class GateTimer extends GateBase {

    private int time = 40;
    private long start = -1;

    private boolean output = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
        left().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "timer";
    }

    @Override
    public void doLogic() {

    }

    @Override
    protected void renderTop(float frame) {

        // renderTop("front", front());
        renderTop("right", right());
        renderTop("back", back());
        renderTop("left", left());

        RenderHelper.renderRedstoneTorch(0, 0, 0, 17 / 16D, back().getInput() == 0);

        double t = 0;
        if (back().getInput() == 0)
            t = -(getWorld().getTotalWorldTime() - start + frame) / (double) time;
        RenderHelper.renderPointer(0, 7 / 16D, 0, 0.5 + t);
    }

    @Override
    public void tick() {

        if (output == true) {
            front().setOutput(0);
            output = false;
        }

        if (back().getInput() == 0) {
            long now = getWorld().getTotalWorldTime();

            if (start == -1)
                start = now;

            if (now - start == time - 2) {
                front().setOutput(15);
                output = true;
                start += time;
            }
        } else {
            start = -1;
        }

        // if (front().getOutput() > 0)
        // spawnBlueParticle(8 / 16D, 6 / 16D, 8 / 16D);
    }
}
