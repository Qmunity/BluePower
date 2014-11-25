package com.bluepowermod.api.bluestone;

import java.util.List;

import uk.co.qmunity.lib.vec.IWorldLocation;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBluestoneDevice extends IWorldLocation {

    public BluestoneColor getBundleColor();

    public List<IBluestoneHandler> getHandlers();

    public IBluestoneDevice getNeighbor(ForgeDirection side);

}
