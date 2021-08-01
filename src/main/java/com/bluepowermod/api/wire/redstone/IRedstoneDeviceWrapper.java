package com.bluepowermod.api.wire.redstone;

import net.minecraft.core.Direction;

public interface IRedstoneDeviceWrapper {

    public IRedstoneDevice getDeviceOnSide(Direction side);

}
