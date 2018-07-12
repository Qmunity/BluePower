package com.bluepowermod.api.power;

import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author MoreThanHidden
 */
public class BlutricityFEStorage extends BlutricityStorage implements IEnergyStorage{


    public BlutricityFEStorage(double max) {
        super(max);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min((int)this.maxEnergy - (int)energy, Math.min((int)this.maxEnergy, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min((int)energy, Math.min((int)this.maxEnergy, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int)energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int)this.maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return (int)energy > 0;
    }

    @Override
    public boolean canReceive() {
        return (int)energy < (int)maxEnergy;
    }
}
