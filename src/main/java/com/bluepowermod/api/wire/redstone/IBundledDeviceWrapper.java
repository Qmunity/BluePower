package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBundledDeviceWrapper {

    public IBundledDevice getBundledDeviceOnSide(ForgeDirection side);

}
