package com.bluepowermod.api.power;

import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * @author MoreThanHidden
 */
public class BlutricityFEStorage extends BlutricityStorage implements IEnergyStorage {


    public BlutricityFEStorage(double max) {
        super(max / 10, 100);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min((int)this.maxEnergy * 10 - (int)energy * 10, Math.min((int)this.maxEnergy * 10, maxReceive));
        if (!simulate)
            energy += energyReceived / 10;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min((int)energy * 10, Math.min((int)this.maxEnergy * 10, maxExtract));
        if (!simulate)
            energy -= energyExtracted / 10;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int)energy * 10;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int)this.maxEnergy * 10;
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
