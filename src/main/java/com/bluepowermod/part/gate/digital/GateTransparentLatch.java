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

package com.bluepowermod.part.gate.digital;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.texture.Layout;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;

public class GateTransparentLatch extends GateSimpleDigital {

    private boolean mirrored = false;

    private GateComponentTorch t1, t2, t3, t4, t5;
    private GateComponentWire w1, w2, w3;

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(10, 2, false);

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable().setOutputOnly();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x215b8d, 4 / 16D, true).setState(false));
        addComponent(t2 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true).setState(false));
        addComponent(t4 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true).setState(false));
        addComponent(t5 = new GateComponentTorch(this, 0x7635c6, 4 / 16D, true).setState(true));

        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(w1 = new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE));
        addComponent(w2 = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w3 = new GateComponentWire(this, 0xd2ae31, RedwireType.BLUESTONE).setPower((byte) 255));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "transparent";
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        GateConnectionDigital in = mirrored ? right() : left();
        GateConnectionDigital out = mirrored ? left() : right();

        buf.shift();

        t4.setState(buf.get(0));
        front().setOutput(buf.get(0));
        out.setOutput(buf.get(0));

        w2.setPower((byte) ((t1.getState() || t5.getState()) ? 255 : 0));
        t3.setState(w2.getPower() == 0);
        w1.setPower((byte) (t3.getState() ? 255 : 0));

        t2.setState(!back().getInput());
        w3.setPower((byte) (t2.getState() ? 255 : 0));
        t1.setState(!in.getInput() && w3.getPower() == 0);

        t5.setState(w1.getPower() == 0 && !back().getInput());

        buf.set(0, w2.getPower() == 0);
    }

    @Override
    public boolean changeMode() {

        mirrored = !mirrored;

        getComponents().clear();
        initConnections();
        initComponents();
        doLogic();

        return true;
    }

    @Override
    public Layout getLayout() {

        Layout layout = super.getLayout();
        if (layout == null)
            return null;
        return layout.getSubLayout(mirrored ? 1 : 0);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        buf.writeToNBT(tag, "buffer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        buf.readFromNBT(tag, "buffer");
    }
}