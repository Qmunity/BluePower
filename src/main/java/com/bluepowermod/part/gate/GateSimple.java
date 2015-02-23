package com.bluepowermod.part.gate;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.part.gate.connection.GateConnectionBase;

public abstract class GateSimple<C extends GateConnectionBase> extends GateBase<C, C, C, C, C, C> implements IGateLogic<GateSimple<C>> {

    @Override
    public GateSimple<C> getGate() {

        return this;
    }

    @Override
    public IGateLogic<? extends GateSimple<C>> logic() {

        return this;
    }

    @Override
    public boolean changeMode() {

        return false;
    }

}
