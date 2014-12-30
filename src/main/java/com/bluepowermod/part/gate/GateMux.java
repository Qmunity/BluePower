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

import net.minecraft.client.resources.I18n;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.util.Color;

public class GateMux extends GateBase {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable();
    }

    @Override
    public String getId() {

        return "multiplexer";
    }

    @Override
    public void renderTop(float frame) {

        // renderTopTexture(FaceDirection.FRONT, false);
        renderTop("left", left().getInput() > 0 || back().getInput() == 0);
        renderTop("right", right().getInput() > 0 || back().getInput() > 0);
        renderTop("back", back().getInput() > 0);
        RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, -2 / 16D, 9D / 16D, back().getInput() == 0);
        boolean frontLeft = !(left().getInput() > 0 || back().getInput() == 0);
        boolean frontRight = !(right().getInput() > 0 || back().getInput() > 0);
        RenderHelper.renderDigitalRedstoneTorch(-4 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, frontRight);
        RenderHelper.renderDigitalRedstoneTorch(4 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, frontLeft);

        renderTop("frontleft", frontLeft);
        renderTop("frontRight", frontRight);
        RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, 4 / 16D, 9D / 16D, !frontLeft && !frontRight);
    }

    @Override
    public void doLogic() {

        boolean selected = back().getInput() > 0;
        int out = 0;

        if (selected) {
            out = left().getInput();
        } else {
            out = right().getInput();
        }

        front().setOutput(out);
    }

    @Override
    public void addWAILABody(List<String> info) {

        info.add(I18n.format("gui.passThrough") + ": " + Color.YELLOW
                + (back().getInput() > 0 ? I18n.format("direction.left") : I18n.format("direction.right")));
    }

}
