package com.bluepowermod.api.gate;

import java.util.Collection;

import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.misc.IFace;

public interface IGate extends IWorldLocation, IFace {

    public Collection<IGateComponent> getComponents();

    public void addComponent(IGateComponent component);

    public IGateConnection bottom();

    public IGateConnection top();

    public IGateConnection left();

    public IGateConnection right();

    public IGateConnection front();

    public IGateConnection back();

    public IGateLogic<?> logic();

}
