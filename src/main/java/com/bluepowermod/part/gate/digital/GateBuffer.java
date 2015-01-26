/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.misc.ShiftingBuffer;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateBuffer extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(1, 3, false);

    private GateComponentTorch t1, t2;
    private GateComponentWire w;

    @Override
    public void initializeConnections() {

        front().enable();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 5 / 16D, true).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true).setState(false));

        addComponent(w = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "buffer";
    }

    @Override
    public void doLogic() {

        buf.set(0, back().getInput());
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        buf.shift();

        boolean mid = !buf.get(0, 1);
        t1.setState(mid);
        w.setPower((byte) (mid ? 255 : 0));

        boolean out = buf.get(0);
        t2.setState(out);
        left().setOutput(out);
        front().setOutput(out);
        right().setOutput(out);
    }
}
