package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.part.gate.GateSimple;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import com.bluepowermod.part.wire.redstone.RedwireType;

public class GateNot extends GateSimple<GateConnectionDigital> {

    private boolean[] power = new boolean[3 * 1];// 3 * numTorches

    private GateComponentTorch t;

    @Override
    protected String getGateType() {

        return "not";
    }

    @Override
    protected void initConnections() {

        front(new GateConnectionDigital(this, Dir.FRONT)).enable().setOutputOnly().setOutput(true);
        left(new GateConnectionDigital(this, Dir.LEFT)).enable().setOutputOnly().setOutput(true);
        right(new GateConnectionDigital(this, Dir.RIGHT)).enable().setOutputOnly().setOutput(true);

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

        power[0] = back().getInput();
    }

    @Override
    public void tick() {

        power[2] = power[1];
        power[1] = power[0];

        t.setState(!power[2]);

        left().setOutput(!power[2]);
        front().setOutput(!power[2]);
        right().setOutput(!power[2]);
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
