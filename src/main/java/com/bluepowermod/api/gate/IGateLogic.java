package com.bluepowermod.api.gate;

public interface IGateLogic<GATE extends IGate> {

    public GATE getGate();

    public void doLogic();

    public void tick();

    public boolean changeMode();

}
