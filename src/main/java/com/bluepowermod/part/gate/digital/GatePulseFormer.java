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

package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.misc.ShiftingBuffer;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GatePulseFormer extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(1, 4, false);
    private GateComponentTorch t1, t2, t3;
    private GateComponentWire w1, w2;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().disable();
        back().enable();
        right().disable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 5 / 16D, true));

        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(w1 = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w2 = new GateComponentWire(this, 0x18DFA5, RedwireType.BLUESTONE));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "pulseformer";
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

        t1.setState(!buf.get(0, 1));
        w2.setPower(!buf.get(0, 1) ? (byte) 255 : 0);
        t2.setState(buf.get(0, 2));

        t3.setState(!buf.get(0, 2) && buf.get(0, 1));

        front().setOutput(!buf.get(0, 2) && buf.get(0, 1));
        w1.setPower(!(!buf.get(0, 2) && buf.get(0, 1)) ? (byte) 255 : 0);
    }
}
