package com.bluepowermod.api.redstone;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

public interface IRedstoneDevice extends IWorldLocation {

    public boolean canConnectStraight(IRedstoneDevice device);

    public boolean canConnectOpenCorner(IRedstoneDevice device);

    public void onConnect(ForgeDirection side, IRedstoneDevice device);

    public void onDisconnect(ForgeDirection side);

    public IRedstoneDevice getDeviceOnSide(ForgeDirection side);

    public byte getRedstonePower(ForgeDirection side);

    public void setRedstonePower(ForgeDirection side, byte power);

    public void onRedstoneUpdate();

    public RedstoneColor getInsulationColor(ForgeDirection side);

    public boolean isNormalBlock();

}
