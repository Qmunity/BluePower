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
import org.jetbrains.annotations.Nullable;

public class RedstoneStorage implements IRedstoneDevice {
    protected final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    protected byte power = 0;
    private final IWorldLocation tile;
    protected Direction input = null;

    public RedstoneStorage(IWorldLocation tile) {
        this.tile = tile;
        redstoneConnections.listen();
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
        if (input != null && input == side) return 0;
        return power;
    }

    @Override
    public byte getVanillaRedstonePower(Direction side) {
        int currentPower = getRedstonePower(side) & 0xFF;
        int remainder = currentPower % 17;
        int level = currentPower / 17;
        if (remainder > 0) level++;
        return (byte) level;
        //return (byte) MathHelper.map(getRedstonePower(side) & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        this.power = power;
    }

    @Override
    public void setInputSide(Direction side) {
        input = side;
    }

    @Override
    public @Nullable Direction getInputSide() {
        return input;
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
