package com.bluepowermod.api.power;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.connect.IConnectionCache;

public interface IPowerHandler {

    public IConnectionCache<? extends IBluePowered> getPowerConnectionCache();

    public float getAmpsStored();

    public float getMaxAmps();

    /**
     * Injects energy into the item.
     *
     * @param amp
     *            The amount of energy to inject
     * @return The amount of energy that has been injected
     */
    public float injectEnergy(float amps, boolean simulate);

    /**
     * Drains energy from the item.
     *
     * @param amp
     *            The amount of energy to drain
     * @return The amount of energy that has been drained
     */
    public float drainEnergy(float amps, boolean simulate);

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public void update();

    public void invalidate();

}
