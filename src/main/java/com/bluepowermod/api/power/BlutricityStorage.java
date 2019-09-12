package com.bluepowermod.api.power;

/**
 * @author MoreThanHidden
 */
public class BlutricityStorage implements IPowerBase{

    double maxEnergy;
    double energy = 0;
    private double current = 0;
    private double maxVoltage;

    public BlutricityStorage(double maxEnergy, double maxVoltage){
        this.maxEnergy = maxEnergy;
        this.maxVoltage = maxVoltage;
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public double getVoltage() {
        return (maxVoltage / 2) + ((maxVoltage / maxEnergy) * energy) / 2;
    }

    @Override
    public double getMaxVoltage() {
        return this.maxVoltage;
    }

    @Override
    public double getCurrent() {
        double existingCurrent = current;
        current = 0;
        return existingCurrent;
    }

    @Override
    public double addEnergy(double energy, boolean simulate) {
        double energyAccepted = 0;
        if (energy > 0)
            energyAccepted = Math.min(this.maxEnergy - this.energy, Math.min(this.maxEnergy, energy));
        else if (energy < 0)
            energyAccepted = -Math.min(this.energy, Math.min(this.maxEnergy, -energy));
        if (!simulate)
            this.energy += energyAccepted;
        current = ((energy / getVoltage()) * energyAccepted);
        return energyAccepted;
    }
}
