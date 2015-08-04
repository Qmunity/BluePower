package com.bluepowermod.api.wire.redstone;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnection;

@Deprecated
public interface IBundledConductor extends IBundledDevice, IRedConductor {
    
    /**
     * Whether or not this device can continue (or start) a propagation on the specified side.
     */
    @Deprecated
    public boolean canPropagateBundledFrom(ForgeDirection fromSide);
    
    @Deprecated
    public static interface IAdvancedBundledConductor extends IBundledConductor {
        
        @Deprecated
        public void propagateBundled(ForgeDirection fromSide, Collection<IConnection<IBundledDevice>> propagation);
    }
}
