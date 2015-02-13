package com.bluepowermod.part.gate.digital;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.misc.ShiftingBuffer;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateXor extends GateSimpleDigital {

    private GateComponentTorch t1, t2, t3;
    private GateComponentWire w;

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(6, 2, false);

    @Override
    protected void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        left().enable();
    }

    @Override
    protected void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true));
        addComponent(t3 = new GateComponentTorch(this, 0x6eade7, 4 / 16D, true).setState(true));

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));
        addComponent(w = new GateComponentWire(this, 0x18dfa5, RedwireType.BLUESTONE).setPower((byte) 255));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    protected String getGateType() {

        return "xor";
    }

    @Override
    public void doLogic() {

        buf.set(0, left().getInput());
        buf.set(1, right().getInput());
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        buf.shift();

        front().setOutput(t1.getState() || t2.getState());

        t3.setState(!buf.get(0) && !buf.get(1));
        w.setPower((byte) (t3.getState() ? 255 : 0));

        t1.setState(!buf.get(0) && !t3.getState());
        t2.setState(!buf.get(1) && !t3.getState());
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
