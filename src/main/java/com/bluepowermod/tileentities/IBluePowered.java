package com.bluepowermod.tileentities;

/**
 * Interface implemented by Blulectric machines.
 * @author MineMaarten
 *
 */
public interface IBluePowered {
    
    /**
     * Called from the client only, when true it'll render the 'powered' icon, if false, it'll render the 'not powered' icon.
     * @return
     */
    public boolean isPowered();
}
