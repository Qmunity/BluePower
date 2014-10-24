package com.bluepowermod.api.bluestone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBluestoneDevice {

    IBluestoneDevice getConnectedDevice(ForgeDirection side, BluestoneConnectionType type);

    void onPowerUpdate(BluestoneConnectionType changed, int oldValue, int newValue);

    void propagate(List<IBluestoneDevice> visited, BluestoneConnectionType type, ForgeDirection from, int oldValue, int newValue);

}
