package com.bluepowermod.api.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

public interface IRedstoneConductor extends IRedstoneDevice, IConductor {

    public Collection<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide);

}
