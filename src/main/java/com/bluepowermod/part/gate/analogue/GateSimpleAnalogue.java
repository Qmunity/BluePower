package com.bluepowermod.part.gate.analogue;

import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.part.gate.GateSimple;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;

public abstract class GateSimpleAnalogue extends GateSimple<GateConnectionAnalogue> {

    @Override
    protected final void initConnections() {

        top(new GateConnectionAnalogue(this, Dir.TOP));
        bottom(new GateConnectionAnalogue(this, Dir.BOTTOM));
        front(new GateConnectionAnalogue(this, Dir.FRONT));
        back(new GateConnectionAnalogue(this, Dir.BACK));
        left(new GateConnectionAnalogue(this, Dir.LEFT));
        right(new GateConnectionAnalogue(this, Dir.RIGHT));

        initializeConnections();
    }

    protected abstract void initializeConnections();

}
