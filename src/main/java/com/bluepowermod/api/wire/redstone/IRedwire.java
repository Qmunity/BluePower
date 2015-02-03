package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;

/**
 * Interface implemented by BluePower's redstone wires. This makes other wires check the wire type before connecting.
 */
public interface IRedwire {

    public RedwireType getRedwireType();

    public static interface IInsulatedRedwire extends IRedwire, IAdvancedRedstoneConductor, IAdvancedBundledConductor {

    }

}
