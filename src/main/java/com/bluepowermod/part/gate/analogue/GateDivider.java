package com.bluepowermod.part.gate.analogue;

import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;

public class GateDivider extends GateSimpleAnalogue {

    private GateComponentTorch t1, t2;
    private GateComponentWire w;

    @Override
    protected String getGateType() {

        return "divider";
    }

    @Override
    protected void initializeConnections() {

        front().enable();
        back().enable();
        left().enable().setOutputOnly();
        right().enable().setOutputOnly();

        back(new GateConnectionAnalogue(this, Dir.BACK)).enable();
    }

    @Override
    protected void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x00FF00, 4 / 16D, false).setState(true));

        addComponent(new GateComponentWire(this, 0x00F0FF, RedwireType.RED_ALLOY).bind(front()));
        addComponent(w = new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY));
        addComponent(new GateComponentWire(this, 0xbbff51, RedwireType.RED_ALLOY).bind(back()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public void doLogic() {

        t1.setState(front().getInput() == 0);
        t2.setState(back().getInput() == 0);
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        double in = front().getInput() & 0xFF;
        double d = back().getInput() & 0xFF;

        double out = d > 0 ? in / d : 0;

        left().setOutput((byte) out);
        right().setOutput((byte) out);
        w.setPower((byte) out);
    }

}
