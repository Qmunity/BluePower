package com.bluepowermod.api.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.IFace;

public interface IFaceRedstoneDevice extends IRedstoneDevice, IFace {

    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device);

}
