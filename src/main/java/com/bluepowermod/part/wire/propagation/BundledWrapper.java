package com.bluepowermod.part.wire.propagation;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.RedstoneColor;

public class BundledWrapper implements IRedstoneDevice {

    private IBundledDevice device;
    private RedstoneColor color;

    public BundledWrapper(IBundledDevice device, RedstoneColor color) {

        this.device = device;
        this.color = color;
    }

    @Override
    public World getWorld() {

        return device.getWorld();
    }

    @Override
    public int getX() {

        return device.getX();
    }

    @Override
    public int getY() {

        return device.getY();
    }

    @Override
    public int getZ() {

        return device.getZ();
    }

    @Override
    public boolean canConnectStraight(IRedstoneDevice device) {

        return false;
    }

    @Override
    public boolean canConnectOpenCorner(IRedstoneDevice device) {

        return false;
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

    }

    @Override
    public void onDisconnect(ForgeDirection side) {

    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        IBundledDevice dev = device.getBundledDeviceOnSide(side);
        if (dev == null)
            return null;

        return new BundledWrapper(dev, color);
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        return device.getBundledPower(side)[color.ordinal()];
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        byte[] pow = device.getBundledPower(side);
        pow[color.ordinal()] = power;
        device.setBundledPower(side, pow);
    }

    @Override
    public void onRedstoneUpdate() {

        device.onBundledUpdate();
    }

    @Override
    public RedstoneColor getInsulationColor(ForgeDirection side) {

        return color;
    }

    @Override
    public boolean isNormalBlock() {

        return device.isNormalBlock();
    }

}
