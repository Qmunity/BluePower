package com.bluepowermod.api.wire.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnection;

@Deprecated
public interface IRedstoneConductor extends IRedstoneDevice, IRedConductor {

    /**
     * Whether or not this device can continue (or start) a propagation run on the specified side.
     */
    @Deprecated
    public boolean canPropagateFrom(ForgeDirection fromSide);

    @Deprecated
    public static interface IAdvancedRedstoneConductor extends IRedstoneConductor {

        @Deprecated
        public void propagate(ForgeDirection fromSide, Collection<IConnection<IRedstoneDevice>> propagation);
    }

}
