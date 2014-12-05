package com.bluepowermod.api.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

public interface IBundledConductor extends IBundledDevice, IConductor {

    public Collection<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide);

}
