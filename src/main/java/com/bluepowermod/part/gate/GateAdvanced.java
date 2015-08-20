package com.bluepowermod.part.gate;

import com.bluepowermod.part.gate.connection.GateConnectionBase;

@SuppressWarnings("unchecked")
public abstract class GateAdvanced<C_BOTTOM extends GateConnectionBase, C_TOP extends GateConnectionBase, C_LEFT extends GateConnectionBase, C_RIGHT extends GateConnectionBase, C_FRONT extends GateConnectionBase, C_BACK extends GateConnectionBase>
        extends GateBase {

    @Override
    public C_FRONT front() {

        return (C_FRONT) super.front();
    }

    @Override
    public C_BACK back() {

        return (C_BACK) super.back();
    }

    @Override
    public C_LEFT left() {

        return (C_LEFT) super.left();
    }

    @Override
    public C_RIGHT right() {

        return (C_RIGHT) super.right();
    }

    @Override
    public C_BOTTOM bottom() {

        return (C_BOTTOM) super.bottom();
    }

    @Override
    public C_TOP top() {

        return (C_TOP) super.top();
    }

}
