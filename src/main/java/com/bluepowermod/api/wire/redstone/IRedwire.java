package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.misc.IWorldLocation;
import net.minecraft.core.Direction;

import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;

/**
 * Interface implemented by BluePower's redstone wires. This makes other wires check the wire type before connecting.
 */
public interface IRedwire extends IWorldLocation {

    RedwireType getRedwireType(Direction side);

    boolean canReceivePower(Direction side);

    boolean canOutputPower(Direction side);


    interface IInsulatedRedwire extends IInsulatedRedstoneDevice, IRedwire, IAdvancedRedstoneConductor,
            IAdvancedBundledConductor {

    }

}
