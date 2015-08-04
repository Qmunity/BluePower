package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneConductor2 extends IRedstoneDevice {

    public int getSignalStrength(ForgeDirection side);

    public void propagate(ForgeDirection fromSide);
}
