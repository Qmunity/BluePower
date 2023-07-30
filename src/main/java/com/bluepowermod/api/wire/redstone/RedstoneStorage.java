package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedstoneStorage implements IRedstoneDevice {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final Level level;
    private final BlockPos blockPos;

    public RedstoneStorage(Level level, BlockPos blockPos) {
        this.level = level;
        this.blockPos = blockPos;
    }


    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {
        return redstoneConnections;
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
    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
