package com.bluepowermod.api.redstone;

import com.bluepowermod.api.misc.IFace;

public interface IFaceRedstoneDevice extends IRedstoneDevice, IFace {

    public boolean canConnectClosedCorner(IRedstoneDevice device);

}
