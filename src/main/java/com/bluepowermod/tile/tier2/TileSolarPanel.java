package com.bluepowermod.tile.tier2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.power.IBluePowered;
import com.bluepowermod.api.power.IPowerHandler;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.tile.TileMachineBase;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileSolarPanel extends TileMachineBase implements IBluePowered {

    private IPowerHandler handler;

    @Override
    public PowerTier getPowerTier() {

        return PowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerHandler getPowerHandler(ForgeDirection side) {

        return getPowerHandler();
    }

    public IPowerHandler getPowerHandler() {

        if (handler == null)
            handler = BPApi.getInstance().getNewPowerHandler(this, 500);

        return handler;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return side == ForgeDirection.DOWN;
    }

    @Override
    public boolean canConnectPower(ForgeDirection side, IBluePowered dev, ConnectionType type) {

        return !side.equals(ForgeDirection.UP);
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {
            // TODO: Add me as a config
            getPowerHandler().injectEnergy(getDaylightStrength() / 10, false);

            getPowerHandler().update();
        }

    }

    public int getDaylightStrength() {

        int i1 = getWorldObj().skylightSubtracted;
        int savedLight = getWorldObj().getSavedLightValue(EnumSkyBlock.Sky, xCoord, yCoord + 1, zCoord);
        i1 = savedLight - i1;
        float f = getWorldObj().getCelestialAngleRadians(1.0F);

        if (f < (float) Math.PI) {
            f += (0.0F - f) * 0.2F;
        } else {
            f += (((float) Math.PI * 2F) - f) * 0.2F;
        }

        i1 = Math.round(i1 * MathHelper.cos(f));

        if (i1 < 0) {
            i1 = 0;
        }

        if (i1 > 15) {
            i1 = 15;
        }

        return i1;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        getPowerHandler().readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        getPowerHandler().writeToNBT(tagCompound);
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (!getWorld().isRemote) {
            getPowerHandler().invalidate();
        }
    }
}
