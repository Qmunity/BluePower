package com.bluepowermod.api.power;

/**
 * @author MineMaarten, Koen Beckers (K4Unl), MoreThanHidden
 */
public interface IPowerBase {

    double getEnergy();

    double getVoltage();
    double getMaxVoltage();
    double getCurrent();

    /**
     * Negative energy for removal
     * @param energy
     * @param simulate when true, no power will be added, but the return value can be used to determine if adding or consuming power is possible.
     * @return the added power.
     */
    double addEnergy(double energy, boolean simulate);

}
