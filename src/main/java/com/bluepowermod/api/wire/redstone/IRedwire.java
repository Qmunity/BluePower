package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;

/**
 * Interface implemented by BluePower's redstone wires. This makes other wires check the wire type before connecting.
 */
public interface IRedwire {

    public RedwireType getRedwireType(ForgeDirection side);

    public static interface IInsulatedRedwire extends IInsulatedRedstoneDevice, IRedwire, IAdvancedRedstoneConductor,
            IAdvancedBundledConductor {

    }

}
