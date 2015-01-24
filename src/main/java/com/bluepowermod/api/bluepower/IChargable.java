package com.bluepowermod.api.bluepower;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface IChargable {

    public BluePowerTier getTier();

    public float getAmpStored();

    public float getMaxAmp();

    public void removeEnergy(float amp);

    public void addEnergy(float amp);

}
