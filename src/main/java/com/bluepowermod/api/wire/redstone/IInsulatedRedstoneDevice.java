package com.bluepowermod.api.wire.redstone;

import net.minecraft.util.Direction;

import com.bluepowermod.api.misc.MinecraftColor;

public interface IInsulatedRedstoneDevice extends IRedstoneDevice {

    /**
     * Gets the insulation color on the specified side. This usually determines whether or not things can connect to it.
     */
    MinecraftColor getInsulationColor(Direction side);

    /**
     * Sets the insulation color only for initialisation.
     */
    void setInsulationColor(MinecraftColor color);
}
