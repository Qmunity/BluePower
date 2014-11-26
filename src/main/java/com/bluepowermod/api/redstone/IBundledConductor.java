package com.bluepowermod.api.redstone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBundledConductor extends IBundledDevice, IConductor {

    public List<IBundledDevice> propagateBundled(ForgeDirection fromSide);

}
