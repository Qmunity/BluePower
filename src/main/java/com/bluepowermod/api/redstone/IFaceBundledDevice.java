package com.bluepowermod.api.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.IFace;

public interface IFaceBundledDevice extends IBundledDevice, IFace {

    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device);

}
