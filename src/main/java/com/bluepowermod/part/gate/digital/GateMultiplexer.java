package com.bluepowermod.part.gate.digital;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.misc.ShiftingBuffer;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateMultiplexer extends GateSimpleDigital {

    private GateComponentTorch t1, t2, t3, t4;
    private GateComponentWire w1, w2;

    private ShiftingBuffer<Boolean> buf = null;

    @Override
    protected void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable();
    }

    @Override
    protected void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x215b8d, 4 / 16D, true).setState(false));
        addComponent(t2 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true).setState(true));
        addComponent(t4 = new GateComponentTorch(this, 0x6F00B5, 5 / 16D, true).setState(false));

        addComponent(w1 = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));
        addComponent(w2 = new GateComponentWire(this, 0xd2ae31, RedwireType.BLUESTONE).setPower((byte) 255));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    protected String getGateType() {

        return "multiplexer";
    }

    @Override
    public void doLogic() {

        if (buf == null) {
            buf = new ShiftingBuffer<Boolean>(5, 2, false);

            buf.set(3, true);
            buf.set(0, true);

            buf.shift();
        }

        buf.set(0, back().getInput());
        buf.set(1, left().getInput());
        buf.set(2, right().getInput());
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        if (buf == null) {
            buf = new ShiftingBuffer<Boolean>(5, 2, false);

            buf.set(3, true);
            buf.set(0, true);

            buf.shift();
        }

        buf.shift();

        t2.setState(!buf.get(0));
        t3.setState(!buf.get(0) && !buf.get(2));

        w2.setPower((byte) (!buf.get(0) ? 255 : 0));
        t1.setState(buf.get(0) && !buf.get(1));

        w1.setPower((byte) (buf.get(3) ? 255 : 0));

        t4.setState(buf.get(4));
        front().setOutput(buf.get(4));

        buf.set(3, (!buf.get(0) && !buf.get(2)) || (buf.get(0) && !buf.get(1)));
        buf.set(4, !buf.get(3));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        buf.writeToNBT(tag, "buffer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        if (buf == null) {
            buf = new ShiftingBuffer<Boolean>(5, 2, false);

            buf.set(3, true);
            buf.set(0, true);

            buf.shift();
        }

        buf.readFromNBT(tag, "buffer");
    }
}
