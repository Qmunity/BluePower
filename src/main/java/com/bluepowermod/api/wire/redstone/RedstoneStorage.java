package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import net.minecraft.util.Direction;

public class RedstoneStorage implements IRedstoneDevice {

    byte power = 0;

    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {
        return null;
    }

    @Override
    public byte getRedstonePower(Direction side) {
        return power;
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }
}
