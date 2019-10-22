package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.util.Direction;

/**
 * @author MoreThanHidden
 */
public class InsulatedRedstoneDevice implements IInsulatedRedstoneDevice {

    private byte power = 0;
    private MinecraftColor color;

    public InsulatedRedstoneDevice(MinecraftColor color){
        this.color = color;
    }

    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {
        return RedstoneApi.INSTANCE.createRedstoneConnectionCache(this);
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

    @Override
    public MinecraftColor getInsulationColor(Direction side) {
        return color;
    }

    @Override
    public void setInsulationColor(MinecraftColor color) {
        this.color = color;
    }
}
