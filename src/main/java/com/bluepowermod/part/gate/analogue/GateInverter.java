package com.bluepowermod.part.gate.analogue;

import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.part.gate.GateSimple;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;
import com.bluepowermod.part.wire.redstone.RedwireType;

public class GateInverter extends GateSimple<GateConnectionAnalogue> {

    private byte[] power = new byte[3];

    private GateComponentTorch t;

    @Override
    protected String getGateType() {

        return "inverter";
    }

    @Override
    protected void initConnections() {

        front(new GateConnectionAnalogue(this, Dir.FRONT)).enable().setOutputOnly().setOutput((byte) 255);
        left(new GateConnectionAnalogue(this, Dir.LEFT)).enable().setOutputOnly().setOutput((byte) 255);
        right(new GateConnectionAnalogue(this, Dir.RIGHT)).enable().setOutputOnly().setOutput((byte) 255);

        back(new GateConnectionAnalogue(this, Dir.BACK)).enable();
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

        power[0] = back().getInput();
    }

    @Override
    public void tick() {

        power[2] = power[1];
        power[1] = power[0];

        byte state = (byte) (255 - (power[2] & 0xFF));

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

}
