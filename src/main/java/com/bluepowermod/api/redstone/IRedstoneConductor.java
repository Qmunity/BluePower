package com.bluepowermod.api.redstone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneConductor extends IRedstoneDevice, IConductor {

    public List<IRedstoneDevice> propagate(ForgeDirection fromSide);

}
