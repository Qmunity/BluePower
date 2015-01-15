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

package com.bluepowermod.part.gate.analog;

import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;

/* Takes a signal and inverts it
 e.g. input str 2 output str 13
 input str 5 output str 10
 input str 0 output str 15
 */

public class GateInverter extends GateBase {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        front().setOutput(15, false);
        right().enable().setOutputOnly();
        right().setOutput(15, false);
        back().enable();
        left().enable().setOutputOnly();
        left().setOutput(15, false);
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false);
        t.setState(true);
        addComponent(t);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "inverter";
    }

    @Override
    public void doLogic() {

        int power = back().getInput();

        left().setOutput(15 - power);
        front().setOutput(15 - power);
        right().setOutput(15 - power);
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected void renderTop(float frame) {
    //
    // renderTop("front", front());
    // renderTop("right", right());
    // renderTop("left", left());
    // renderTop("back", back());
    //
    // RenderHelper.renderAnalogRedstoneTorch(0, 0, 0, 12 / 16D, back().getInput() == 0);
    // }
    //
    // @Override
    // public void tick() {
    //
    // if (front().getOutput() > 0)
    // spawnRedParticle(8 / 16D, 6 / 16D, 8 / 16D);
    // }

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
