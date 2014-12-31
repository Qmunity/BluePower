/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import com.bluepowermod.client.render.RenderHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateNot extends GateBase {

    private boolean power = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
        left().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "not";
    }

    @Override
    public void doLogic() {

        power = back().getInput() > 0;

        left().setOutput(!power ? 15 : 0);
        front().setOutput(!power ? 15 : 0);
        right().setOutput(!power ? 15 : 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void renderTop(float frame) {

        renderTop("front", front());
        renderTop("right", right());
        renderTop("back", back());
        renderTop("left", left());

        RenderHelper.renderDigitalRedstoneTorch(0, 0, 0, 12 / 16D, back().getInput() == 0);
    }

    @Override
    public void tick() {

        if (front().getOutput() > 0)
            spawnBlueParticle(8 / 16D, 6 / 16D, 8 / 16D);
    }

    @Override
    protected boolean changeMode() {

        if (left().isEnabled() && front().isEnabled() && right().isEnabled()) {
            right().disable();
        } else if (left().isEnabled() && front().isEnabled()) {
            front().disable();
            right().enable();
        } else if (left().isEnabled() && right().isEnabled()) {
            left().disable();
            front().enable();
        } else if (front().isEnabled() && right().isEnabled()) {
            left().enable();
            front().disable();
            right().disable();
        } else if (left().isEnabled()) {
            left().disable();
            front().enable();
        } else if (front().isEnabled()) {
            front().disable();
            right().enable();
        } else {
            left().enable();
            front().enable();
        }
        return true;
    }
}
