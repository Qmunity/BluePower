package com.bluepowermod.api.bluestone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.vec.IWorldLocation;

public interface IBluestoneDevice extends IWorldLocation {

    public BluestoneColor getBundleColor();

    public List<IBluestoneHandler> getHandlers();

    public IBluestoneDevice getNeighbor(ForgeDirection side);

}
