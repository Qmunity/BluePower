package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.part.gate.GateSimple;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;

public abstract class GateSimpleDigital extends GateSimple<GateConnectionDigital> {

    @Override
    protected final void initConnections() {

        top(new GateConnectionDigital(this, Dir.TOP));
        bottom(new GateConnectionDigital(this, Dir.BOTTOM));
        front(new GateConnectionDigital(this, Dir.FRONT));
        back(new GateConnectionDigital(this, Dir.BACK));
        left(new GateConnectionDigital(this, Dir.LEFT));
        right(new GateConnectionDigital(this, Dir.RIGHT));

        initializeConnections();
    }

    protected abstract void initializeConnections();

}
