package com.bluepowermod.api.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBluestoneHandler {

    public IBluestoneDevice getDevice();

    public BluestoneColor getInsulationColor();

    public int getConductionMap();

    public boolean canConnect(IBluestoneDevice device);

    public IBluestoneHandler getConnectedHandler(ForgeDirection side);

    public void refreshConnections(boolean spread);

    public void onUpdate(int network, int newValue);

    public int getInput(ForgeDirection side);

    public int getPower(int network);

}
