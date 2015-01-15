/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;

public class GateBuffer extends GateBase {

    private boolean power = false;

    @Override
    public void initializeConnections() {

        front().enable();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x0000FF, 5 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true);
        t2.setState(false);
        addComponent(t2);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "buffer";
    }

    @Override
    public void doLogic() {

        power = back().getInput() > 0;

        left().setOutput(power ? 15 : 0);
        front().setOutput(power ? 15 : 0);
        right().setOutput(power ? 15 : 0);
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected void renderTop(float frame) {
    //
    // renderTop("center", back().getInput() == 0 ? "on" : "off");
    // renderTop("right", right());
    // renderTop("back", back());
    // renderTop("left", left());
    //
    // RenderHelper.renderDigitalRedstoneTorch(0, 0, 0, 13 / 16D, back().getInput() == 0);
    // RenderHelper.renderDigitalRedstoneTorch(0, 0, 4 / 16D, 14 / 16D, back().getInput() > 0);
    // }
    //
    // @Override
    // public void tick() {
    //
    // if (front().getOutput() > 0)
    // spawnBlueParticle(8 / 16D, 8 / 16D, 8 / 16D);
    // }
}
