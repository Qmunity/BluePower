package com.bluepowermod.compat.fmp;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class FMPRedstoneDevice implements IFaceRedstoneDevice {

    private TMultiPart part;
    private IRedstoneDevice[] devices = new IRedstoneDevice[6];

    public FMPRedstoneDevice(TMultiPart part) {

        this.part = part;
    }

    @Override
    public World getWorld() {

        return part.world();
    }

    @Override
    public int getX() {

        return part.x();
    }

    @Override
    public int getY() {

        return part.y();
    }

    @Override
    public int getZ() {

        return part.z();
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;
        if (!(device instanceof IFaceRedstoneDevice))
            return false;
        if (((IFaceRedstoneDevice) device).getFace() != getFace())
            return false;

        return ((IRedstonePart) part).canConnectRedstone(side.ordinal());
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return false;
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;
        if (!(device instanceof IFaceRedstoneDevice))
            return false;
        if (((IFaceRedstoneDevice) device).getFace() != side)
            return false;

        return ((IRedstonePart) part).canConnectRedstone(getFace().ordinal());
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return;

        devices[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        IFaceRedstonePart p = ((IFaceRedstonePart) part);

        if (part.tile() == null && part.world() == null)
            return 0;

        int pow = Math.max(p.weakPowerLevel(side.ordinal()), p.strongPowerLevel(side.ordinal()));

        return (byte) MathHelper.map(pow, 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

        if (part.tile() != null && part.world() != null)
            part.onNeighborChanged();
    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public ForgeDirection getFace() {

        return ForgeDirection.getOrientation(((IFaceRedstonePart) part).getFace());
    }

}
