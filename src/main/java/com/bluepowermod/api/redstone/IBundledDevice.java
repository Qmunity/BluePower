package com.bluepowermod.api.redstone;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.misc.MinecraftColor;

public interface IBundledDevice extends IWorldLocation {

    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device);

    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device);

    public void onConnect(ForgeDirection side, IBundledDevice device);

    public void onDisconnect(ForgeDirection side);

    public IBundledDevice getBundledDeviceOnSide(ForgeDirection side);

    public byte[] getBundledPower(ForgeDirection side);

    public void setBundledPower(ForgeDirection side, byte[] power);

    public void onBundledUpdate();

    public MinecraftColor getBundledColor();

    public boolean isBundled();

    public boolean isNormalBlock();

}
