package com.bluepowermod.api.redstone;

import java.util.EnumSet;

import com.bluepowermod.api.misc.MinecraftColor;

public interface IBundledUpdateHandler {

    /**
     * Whether or not power propagation should happen after a block update. This gets called when a bundled wire receives a block update.<br/>
     * This can be used for things like ComputerCraft suport, because computers do block updates when their bundled output changes.
     */
    public boolean shouldPropagateOnBlockUpdate(IBundledDevice device);

    /**
     * This method only gets called if shouldPropagateOnBlockUpdate returns true.
     */
    public EnumSet<MinecraftColor> getColorsToUpdate(IBundledDevice device);

}
