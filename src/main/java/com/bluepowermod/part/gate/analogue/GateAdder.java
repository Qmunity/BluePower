package com.bluepowermod.part.gate.analogue;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateAdder extends GateSimpleAnalogue {

    private GateComponentTorch t1, t2, t3, t4;
    private GateComponentWire wire;

    @Override
    protected String getGateType() {

        return "adder";
    }

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x215b8d, 4 / 16D, false).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false).setState(true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, false).setState(true));
        addComponent(t4 = new GateComponentTorch(this, 0x6F00B5, 5 / 16D, false).setState(false));

        addComponent(wire = new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        int in = (left().getInput() & 0xFF) + (back().getInput() & 0xFF) + (right().getInput() & 0xFF);

        front().setOutput((byte) Math.min(in, 255));

        t1.setState(left().getInput() == 0);
        t2.setState(back().getInput() == 0);
        t3.setState(right().getInput() == 0);

        wire.setPower((byte) (255 - (front().getOutput() & 0xFF)));
        t4.setState(front().getOutput() != 0);
    }

}
