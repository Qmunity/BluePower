package com.bluepowermod.helper;

import com.bluepowermod.api.power.IPowerBase;

/**
 * @author MoreThanHidden
 */
public class EnergyHelper {

    /**
     * Balance the energy of two given IPowerBase storage.
     * @param storage1
     * @param storage2
     */
    public static void balancePower(IPowerBase storage1, IPowerBase storage2){
        if(storage1.getVoltage() != storage2.getVoltage()) {
            double diff = ((storage1.getVoltage() * storage1.getMaxEnergy() + storage2.getVoltage() * storage2.getMaxEnergy()) / (storage1.getMaxEnergy() + storage2.getMaxEnergy()));
            storage1.addEnergy((diff * (storage1.getMaxEnergy() / storage1.getMaxVoltage()) - storage1.getEnergy()), false);
            storage2.addEnergy((diff * (storage2.getMaxEnergy() / storage2.getMaxVoltage()) - storage2.getEnergy()), false);
        }
    }

}
