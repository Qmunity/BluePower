package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneDeviceWrapper {

    public IRedstoneDevice getDeviceOnSide(ForgeDirection side);

}
