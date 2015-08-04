package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.MinecraftColor;

public interface IBundledConductor2 {

    public int getSignalStrength(ForgeDirection side, MinecraftColor color);

    public void propagateBundled(ForgeDirection fromSide);
}
