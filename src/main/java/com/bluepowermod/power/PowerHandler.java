package com.bluepowermod.power;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.power.IBluePowered;
import com.bluepowermod.api.power.IPowerHandler;

public final class PowerHandler implements IPowerHandler {

    private final IBluePowered device;
    private final float max;

    private float stored = 0;

    public PowerHandler(IBluePowered device, float maxAmps) {

        this.device = device;
        this.max = maxAmps;
    }

    @Override
    public IConnectionCache<? extends IBluePowered> getPowerConnectionCache() {

        return null;
    }

    @Override
    public float getAmpsStored() {

        return stored;
    }

    @Override
    public float getMaxAmps() {

        return max;
    }

    @Override
    public float injectEnergy(float amps, boolean simulate) {

        float injected = Math.min(getAmpsStored() + amps, getMaxAmps()) - getAmpsStored();
        if (!simulate)
            stored += injected;
        return injected;
    }

    @Override
    public float drainEnergy(float amps, boolean simulate) {

        float drained = getAmpsStored() - Math.max(getAmpsStored() - amps, 0);
        if (!simulate)
            stored -= drained;
        return drained;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setFloat("power_stored", getAmpsStored());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        stored = tag.getFloat("power_stored");
    }

    @Override
    public void update() {

    }

    @Override
    public void invalidate() {

    }

}
