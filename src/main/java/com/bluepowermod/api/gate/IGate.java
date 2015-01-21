package com.bluepowermod.api.gate;

import java.util.Collection;

public interface IGate<C_BOTTOM extends IGateConnection, C_TOP extends IGateConnection, C_LEFT extends IGateConnection, C_RIGHT extends IGateConnection, C_FRONT extends IGateConnection, C_BACK extends IGateConnection> {

    public Collection<IGateComponent> getComponents();

    public void addComponent(IGateComponent component);

    public C_BOTTOM bottom();

    public C_TOP top();

    public C_LEFT left();

    public C_RIGHT right();

    public C_FRONT front();

    public C_BACK back();

    public IGateLogic<?> logic();

}
