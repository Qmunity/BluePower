package com.bluepowermod.api.wire.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnection;

public interface IRedstoneConductor extends IRedstoneDevice, IRedConductor {

    /**
     * Whether or not this device can continue (or start) a propagation on the specified side.
     */
    public boolean canPropagateFrom(ForgeDirection fromSide);

    public static interface IAdvancedRedstoneConductor extends IRedstoneConductor {

        public void propagate(ForgeDirection fromSide, Collection<IConnection<IRedstoneDevice>> propagation);
    }

}
