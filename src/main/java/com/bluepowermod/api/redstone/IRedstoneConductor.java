package com.bluepowermod.api.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneConductor extends IRedstoneDevice, IConductor {

    public Collection<IRedstoneDevice> propagate(ForgeDirection fromSide);

}
