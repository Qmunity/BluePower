package com.bluepowermod.part.gate;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.part.gate.connection.GateConnectionBase;

@SuppressWarnings("unchecked")
public abstract class GateSimple<CONNECTION extends GateConnectionBase> extends GateBase implements IGateLogic<GateSimple<CONNECTION>> {

    @Override
    public GateSimple<CONNECTION> getGate() {

        return this;
    }

    @Override
    public IGateLogic<? extends GateSimple<CONNECTION>> logic() {

        return this;
    }

    @Override
    public CONNECTION front() {

        return (CONNECTION) super.front();
    }

    @Override
    public CONNECTION back() {

        return (CONNECTION) super.back();
    }

    @Override
    public CONNECTION left() {

        return (CONNECTION) super.left();
    }

    @Override
    public CONNECTION right() {

        return (CONNECTION) super.right();
    }

    @Override
    public CONNECTION bottom() {

        return (CONNECTION) super.bottom();
    }

    @Override
    public CONNECTION top() {

        return (CONNECTION) super.top();
    }

    @Override
    public boolean changeMode() {

        return false;
    }

}
