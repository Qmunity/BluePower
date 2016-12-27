package com.bluepowermod.api.wire.redstone;

import net.minecraft.util.EnumFacing;;

public interface IRedstoneDeviceWrapper {

    public IRedstoneDevice getDeviceOnSide(EnumFacing side);

}
