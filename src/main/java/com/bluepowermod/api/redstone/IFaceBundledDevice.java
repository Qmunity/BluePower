package com.bluepowermod.api.redstone;

import com.bluepowermod.api.misc.IFace;

public interface IFaceBundledDevice extends IBundledDevice, IFace {

    public boolean canConnectBundledClosedCorner(IBundledDevice device);

}
