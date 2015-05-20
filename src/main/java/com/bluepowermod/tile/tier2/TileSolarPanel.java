package com.bluepowermod.tile.tier2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.tile.TileMachineBase;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileSolarPanel extends TileMachineBase implements IPowered, IFace {

    private IPowerBase handler = BPApi.getInstance().getPowerApi().createPowerHandler(this);

    @Override
    public ForgeDirection getFace() {

        return ForgeDirection.DOWN;
    }

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public PowerTier getPowerTier() {

        return PowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerBase getPowerHandler(ForgeDirection side) {

        return handler;
    }

    @Override
    public boolean canConnectPower(ForgeDirection side, IPowered dev, ConnectionType type) {

        if (side == ForgeDirection.UP)
            return false;
        if (type == ConnectionType.OPEN_CORNER && side == ForgeDirection.DOWN)
            return false;
        if (dev instanceof IFace && type == ConnectionType.OPEN_CORNER && dev.getY() == getY() && ((IFace) dev).getFace() != ForgeDirection.DOWN)
            return false;

        return true;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return side == ForgeDirection.DOWN;
    }

    @Override
    public float getMaxPowerStorage() {

        return 500;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {
            // TODO: Add me as a config
            handler.addEnergy(getDaylightStrength() / 10);

            handler.update();
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

        handler.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        handler.writeToNBT(tagCompound);
    }

    @Override
    public void invalidate() {

        super.invalidate();
        handler.disconnect();
    }

    @Override
    public void onNeighborBlockChanged() {

        super.onNeighborBlockChanged();
        handler.onNeighborUpdate();
    }

    @Override
    protected void onTileLoaded() {

        super.onTileLoaded();
        handler.onNeighborUpdate();
    }

}
