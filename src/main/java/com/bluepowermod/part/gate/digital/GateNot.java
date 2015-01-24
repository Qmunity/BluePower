package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import com.bluepowermod.part.wire.redstone.RedwireType;

public class GateNot extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(1, 3, false);

    private GateComponentTorch t;

    @Override
    protected String getGateType() {

        return "not";
    }

    @Override
    protected void initializeConnections() {

        front().enable().setOutputOnly().setOutput(true);
        left().enable().setOutputOnly().setOutput(true);
        right().enable().setOutputOnly().setOutput(true);

        back(new GateConnectionDigital(this, Dir.BACK)).enable();
    }

    @Override
    protected void initComponents() {

        t = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t.setState(true);
        addComponent(t);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public void doLogic() {

        buf.set(0, back().getInput());
    }

    @Override
    public void tick() {

        buf.shift();

        boolean pow = buf.get(0);

        t.setState(!pow);

        left().setOutput(!pow);
        front().setOutput(!pow);
        right().setOutput(!pow);
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

}
