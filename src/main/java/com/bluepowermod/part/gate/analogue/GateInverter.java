package com.bluepowermod.part.gate.analogue;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;

public class GateInverter extends GateSimpleAnalogue {

    private ShiftingBuffer<Byte> buf = new ShiftingBuffer<Byte>(1, 3, (byte) 0);

    private GateComponentTorch t;

    @Override
    protected String getGateType() {

        return "inverter";
    }

    @Override
    protected void initializeConnections() {

        front().enable().setOutputOnly().setOutput((byte) 255);
        left().enable().setOutputOnly().setOutput((byte) 255);
        right().enable().setOutputOnly().setOutput((byte) 255);

        back(new GateConnectionAnalogue(this, Dir.BACK)).enable();
    }

    @Override
    protected void initComponents() {

        addComponent(t = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false).setState(true));

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
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

        byte state = (byte) (255 - (buf.get(0) & 0xFF));

        t.setState((state & 0xFF) > 0);

        left().setOutput(state);
        front().setOutput(state);
        right().setOutput(state);
    }

    @Override
    public boolean changeMode() {

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
