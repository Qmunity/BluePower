package com.bluepowermod.api.gate;

public interface IGateLogic<G extends IGate<?, ?, ?, ?, ?, ?>> {

    public G getGate();

    public void doLogic();

    public void tick();

    public boolean changeMode();

}
