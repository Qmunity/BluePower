package com.bluepowermod.tileentities.tier2;

import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.tileentities.TileMachineBase;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileSolarPanel extends TileMachineBase implements IBluePowered {

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public BluePowerTier getTier() {

        return BluePowerTier.MEDIUMVOLTAGE;
    }
}
