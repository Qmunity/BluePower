package com.bluepowermod.api.power;

/**
 * @author MoreThanHidden
 */
public class BlutricityStorage implements IPowerBase{

    double maxEnergy;
    double energy = 0;

    public BlutricityStorage(double max){
        this.maxEnergy = max;
    }

    @Override
    public double getVoltage() {
        return this.energy;
    }

    @Override
    public double getMaxVoltage() {
        return this.maxEnergy;
    }

    @Override
    public double addEnergy(double energy, boolean simulate) {
        double energyAccepted = 0;
        if (energy > 0)
            energyAccepted = Math.min(this.maxEnergy - this.energy, Math.min(this.maxEnergy, energy));
        if (energy < 0)
            energyAccepted = -Math.min(this.energy, Math.min(this.maxEnergy, -energy));
        if (!simulate)
            this.energy += energyAccepted;
        return energyAccepted;
    }

}
