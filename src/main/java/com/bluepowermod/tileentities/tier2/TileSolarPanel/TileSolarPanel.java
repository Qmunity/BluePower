package com.bluepowermod.tileentities.tier2.TileSolarPanel;

import com.bluepowermod.api.power.IBluePowerGen;
import com.bluepowermod.tileentities.IBluePowered;
import com.bluepowermod.tileentities.TileMachineBase;


/**
 * @author Koen Beckers (K4Unl)
 */
public class TileSolarPanel extends TileMachineBase implements IBluePowered, IBluePowerGen {


    @Override
    public boolean isPowered() {

        return false;
    }
}
