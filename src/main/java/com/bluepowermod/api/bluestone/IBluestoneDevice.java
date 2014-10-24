package com.bluepowermod.api.bluestone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBluestoneDevice {

    IBluestoneDevice getConnectedDevice(ForgeDirection side);

    void onPowerUpdate(int network, int oldValue, int newValue);

    void listConnected(List<IBluestoneDevice> visited, BluestoneColor insulationColor, ForgeDirection from);

    boolean canConnect(BluestoneColor insulationColor, BluestoneColor bundleColor);

}
