package com.bluepowermod.tileentities.tier2;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.tileentities.TileMachineBase;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileSolarPanel extends TileMachineBase implements IBluePowered {

    private IPowerBase handler;

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public BluePowerTier getTier() {

        return BluePowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerBase getHandler() {
        if(handler == null){
            handler = BPApi.getInstance().getNewPowerHandler();
            handler.init(this);
        }
        return handler;
    }

    @Override
    public void updateEntity(){

        super.updateEntity();

        if(!getWorldObj().isRemote){
            //TODO: Add me as a config
            if(getHandler().getAmpStored() < 2000){
                getHandler().addEnergy(getWorldObj().getBlockLightValue(xCoord, yCoord+1, zCoord));
                BluePower.log.info(getHandler().getAmpStored() + "");
            }
        }

    }
}
