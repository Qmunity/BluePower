package com.bluepowermod.api.wire.redstone;

import java.util.Collection;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnection;

public interface IRedstoneConductor extends IRedstoneDevice, IRedConductor {

    /**
     * Whether or not this device can continue (or start) a propagation on the specified side.
     */
    public boolean canPropagateFrom(ForgeDirection fromSide);

    public static interface IAdvancedRedstoneConductor extends IRedstoneConductor {

        /**
         * Returns a list of all the connections the propagation code should visit when propagating from the specified side. It can inculde that
         * side's connection, it will just get ignored.
         *
         * The returned entries should have the connection as a key, and as a value, whether or not the propagation to that connection should be done
         * separately, after this one. This is useful in cases like Red Alloy and Infused Teslatite wires' power transmission, where one can connect
         * and power the other, but they cannot be ran on the same propagation run because one is lossy and the other one isn't.
         */
        public Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide);
    }

}
