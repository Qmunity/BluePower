package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.helper.RedstoneHelper;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedstoneStorage implements IRedstoneDevice, IRedConductor {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final IRedwire wire;
    private final Direction face;
    private Pair<Direction, Byte> input = null;

    public RedstoneStorage(IRedwire wire, Direction face) {
        this.wire = wire;
        this.face = face;
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
        if (input != null && input.first() == side) return 0;
        return power;
    }

    @Override
    public byte getVanillaRedstonePower(Direction side) {
        if (side != null && !wire.canOutputPower(side)) return 0;
        return (byte) MathHelper.map(getRedstonePower(side) & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {
        if (this.getLevel() == null) return;
        // Don't to anything if propagation-related stuff is going on
        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;
        if (getLevel().isClientSide()) return;

        //RedstoneApi.getInstance().getRedstonePropagator(this, face).propagate();
    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }

    @Override
    public BlockPos getBlockPos() {
        return wire.getBlockPos();
    }

    @Override
    public Level getLevel() {
        return wire.getLevel();
    }

    @Override
    public boolean hasLoss(Direction side) {
        return wire.getRedwireType(side).hasLoss();
    }

    @Override
    public boolean isAnalogue(Direction side) {
        return wire.getRedwireType(side).isAnalogue();
    }
}
