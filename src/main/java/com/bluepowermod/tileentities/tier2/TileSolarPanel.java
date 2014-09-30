package com.bluepowermod.tileentities.tier2;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.bluepower.IPowerBase;
import com.bluepowermod.tileentities.TileMachineBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

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
            handler = BPApi.getInstance().getNewPowerHandler(this, 2000);
        }
        return handler;
    }

    @Override
    public boolean canConnectTo(ForgeDirection dir) {

        return !dir.equals(ForgeDirection.UP);
    }

    @Override
    public void updateEntity(){

        super.updateEntity();

        if(!getWorldObj().isRemote){
            //TODO: Add me as a config
            getHandler().addEnergy(getDaylightStrength() / 10);

            getHandler().update();
        }

    }

    public int getDaylightStrength(){
        int i1 =  getWorldObj().skylightSubtracted;
        int savedLight = getWorldObj().getSavedLightValue(EnumSkyBlock.Sky, xCoord, yCoord+1, zCoord);
        i1 = savedLight - i1;
        float f = getWorldObj().getCelestialAngleRadians(1.0F);

        if (f < (float)Math.PI){
            f += (0.0F - f) * 0.2F;
        } else {
            f += (((float)Math.PI * 2F) - f) * 0.2F;
        }

        i1 = Math.round((float)i1 * MathHelper.cos(f));

        if (i1 < 0){
            i1 = 0;
        }

        if (i1 > 15){
            i1 = 15;
        }

        return i1;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        getHandler().readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        getHandler().writeToNBT(tagCompound);
    }
}
