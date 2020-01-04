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
    public double getMaxEnergy() {
        return this.maxEnergy;
    }

    @Override
    public double getVoltage() {
        return (maxVoltage / maxEnergy) * energy;
    }

    @Override
    public double getMaxVoltage() {
        return this.maxVoltage;
    }

    @Override
    public double getCurrent() {
        return current * 5;
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
        current += energyAccepted > 0 ? energyAccepted : -energyAccepted;
        return energyAccepted;
    }

    public void resetCurrent(){
        current = 0;
    }

}
