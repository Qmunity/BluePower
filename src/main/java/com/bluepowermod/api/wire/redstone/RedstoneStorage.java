package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class RedstoneStorage implements IRedstoneDevice, IRedConductor {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final Supplier<Level> level;
    private final BlockPos blockPos;
    private final Direction face;
    private final RedwireType type;

    public RedstoneStorage(Supplier<Level> level, BlockPos blockPos, Direction face, RedwireType type) {
        this.level = level;
        this.blockPos = blockPos;
        this.face = face;
        this.type = type;
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
        if (this.level.get() == null) return;
        // Don't to anything if propagation-related stuff is going on
        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;
        if (level.get().isClientSide()) return;

        //RedstoneApi.getInstance().getRedstonePropagator(this, face).propagate();
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
        return level.get();
    }

    @Override
    public boolean hasLoss(Direction side) {
        return type.hasLoss();
    }

    @Override
    public boolean isAnalogue(Direction side) {
        return type.isAnalogue();
    }
}
