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

public class GateXnor extends GateBase {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        left().enable();
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true);
        t2.setState(false);
        addComponent(t2);
        GateComponentTorch t3 = new GateComponentTorch(this, 0x3e94dc, 5 / 16D, true);
        t3.setState(true);
        addComponent(t3);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));
        addComponent(new GateComponentWire(this, 0x18DFA5, RedwireType.BLUESTONE));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "xnor";
    }

    @Override
    public void doLogic() {

        boolean l = left().getInput() > 0;
        boolean r = right().getInput() > 0;

        front().setOutput(((l && !r) || (!l && r)) ? 0 : 15);
    }

    @Override
    protected boolean changeMode() {

        return true;
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected void renderTop(float frame) {
    //
    // boolean l = left().getInput() > 0;
    // boolean r = right().getInput() > 0;
    // boolean c = !l && !r;
    //
    // renderTop("frontleft", (!l && !c) ? "on" : "off");
    // renderTop("frontright", (!r && !c) ? "on" : "off");
    // renderTop("right", right());
    // renderTop("left", left());
    // renderTop("center", c ? "on" : "off");
    //
    // RenderHelper.renderDigitalRedstoneTorch(4 / 16D, 0, 0, 12 / 16D, !l && !c);
    // RenderHelper.renderDigitalRedstoneTorch(-4 / 16D, 0, 0, 12 / 16D, !r && !c);
    //
    // RenderHelper.renderDigitalRedstoneTorch(0 / 16D, 0, -4 / 16D, 13 / 16D, c);
    // RenderHelper.renderDigitalRedstoneTorch(0 / 16D, 0, 4 / 16D, 13 / 16D, !((!l && !c) || (!r && !c)));
    // }

    @Override
    public void tick() {

        // if (front().getOutput() > 0)
        // spawnBlueParticle(8 / 16D, 8 / 16D, 8 / 16D);
    }
}
