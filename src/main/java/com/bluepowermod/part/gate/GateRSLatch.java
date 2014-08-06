/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 *
 * @author Quetzi
 */

package com.bluepowermod.part.gate;

import java.util.List;

import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

public class GateRSLatch extends GateBase {

    private boolean stateLeft = false;
    private boolean stateRight = true;

    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        front.enable();
        front.setOutput();

        left.enable();
        left.setInput();

        right.enable();
        right.setOutput();

        back.enable();
        back.setOutput();

    }

    @Override
    public String getGateID() {
        return "rs";
    }

    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {

        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/left_" + (stateLeft ? "on" : "off") + ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/right_" + (stateRight ? "on" : "off") + ".png");
        RenderHelper.renderRedstoneTorch(-1D / 8D, 1D / 8D, 2D / 8D, 9D / 16D, !stateLeft);
        RenderHelper.renderRedstoneTorch(1D / 8D, 1D / 8D, -2D / 8D, 9D / 16D, !stateRight);
    }

    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

    }

    @Override
    public void addWailaInfo(List<String> info) {

    }
}
