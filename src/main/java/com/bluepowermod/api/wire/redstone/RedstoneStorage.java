package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.IWorldLocation;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedstoneStorage implements IRedstoneDevice {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final IWorldLocation tile;
    private Pair<Direction, Byte> input = null;

    public RedstoneStorage(IWorldLocation tile) {
        this.tile = tile;
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
        return (byte) MathHelper.map(getRedstonePower(side) & 0xFF, 0, 255, 0, 15);
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
        return tile.getBlockPos();
    }

    @Override
    public Level getLevel() {
        return tile.getLevel();
    }

}
