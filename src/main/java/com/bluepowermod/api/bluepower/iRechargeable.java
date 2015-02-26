package com.bluepowermod.api.bluepower;

/**
 * @author Koen Beckers (K4Unl)
 */
public interface iRechargeable {

    public BluePowerTier getTier();

    public float getAmpStored();

    public float getMaxAmp();

    /**!
     * Removes energy from the rechargeable item.
     * @param amp The amount of energy to drain
     * @return The amount of energy that has been drained
     */
    public float removeEnergy(float amp);

    /**!
     * Adds energy to the rechargeable item.
     * @param amp The amount of energy to add
     * @return The amount of energy that has been added
     */
    public float addEnergy(float amp);

}
