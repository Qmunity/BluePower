/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import com.bluepowermod.client.renderers.RenderHelper;

public class GateXor extends GateBase {

    private boolean power = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        left().enable();
    }

    @Override
    public String getId() {

        return "xor";
    }

    @Override
    public void doLogic() {

        boolean l = left().getInput() > 0;
        boolean r = right().getInput() > 0;

        front().setOutput(((l && !r) || (!l && r)) ? 15 : 0);
    }

    @Override
    protected boolean changeMode() {

        return true;
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

    @Override
    protected void renderTop(float frame) {

        boolean l = left().getInput() > 0;
        boolean r = right().getInput() > 0;
        boolean c = !l && !r;

        renderTop("front", front());
        renderTop("right", right());
        renderTop("left", left());
        renderTop("center", c ? "on" : "off");

        RenderHelper.renderRedstoneTorch(4 / 16D, 0, 0, 12 / 16D, !l && !c);
        RenderHelper.renderRedstoneTorch(-4 / 16D, 0, 0, 12 / 16D, !r && !c);

        RenderHelper.renderRedstoneTorch(0 / 16D, 0, -4 / 16D, 13 / 16D, c);
    }

    @Override
    public void tick() {

        // if (front().getOutput() > 0)
        // spawnBlueParticle(8 / 16D, 8 / 16D, 8 / 16D);
    }
}
