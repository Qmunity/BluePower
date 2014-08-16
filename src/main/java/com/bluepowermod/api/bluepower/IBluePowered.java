package com.bluepowermod.api.bluepower;

/**
 * Interface implemented by Blulectric machines.
 * @author MineMaarten & Koen Beckers (K4Unl)
 *
 */
public interface IBluePowered {
    
    /**
     * @author MineMaarten
     * Called from the client only, when true it'll render the 'powered' icon, if false, it'll render the 'not powered' icon.
     * @return
     */
    public boolean isPowered();


    /**
     * @author Koen Beckers (K4Unl)
     * Gets the tier of the machine.
     * @return
     */
    public BluePowerTier getTier();

}
