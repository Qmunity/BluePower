package com.bluepowermod.api.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBundledConductor extends IBundledDevice, IConductor {

    public Collection<IBundledDevice> propagateBundled(ForgeDirection fromSide);

}
