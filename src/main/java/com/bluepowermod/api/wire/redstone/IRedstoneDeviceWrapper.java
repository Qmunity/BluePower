package com.bluepowermod.api.wire.redstone;

import net.minecraft.util.Direction;

public interface IRedstoneDeviceWrapper {

    public IRedstoneDevice getDeviceOnSide(Direction side);

}
