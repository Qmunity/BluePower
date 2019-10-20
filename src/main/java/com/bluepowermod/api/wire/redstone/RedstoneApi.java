package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MoreThanHidden
 */
public class RedstoneApi implements IRedstoneApi {
    public static RedstoneApi INSTANCE = new RedstoneApi();

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, BlockPos pos, Direction face, Direction side) {
        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, BlockPos pos, Direction face, Direction side) {
        return null;
    }

    @Override
    public void registerRedstoneProvider(IRedstoneProvider provider) {

    }

    @Override
    public boolean shouldWiresOutputPower(boolean lossy) {
        return false;
    }

    @Override
    public void setWiresOutputPower(boolean shouldWiresOutputPower, boolean lossy) {

    }

    @Override
    public boolean shouldWiresHandleUpdates() {
        return false;
    }

    @Override
    public void setWiresHandleUpdates(boolean shouldWiresHandleUpdates) {

    }

    @Override
    public IConnection<IRedstoneDevice> createConnection(IRedstoneDevice a, IRedstoneDevice b, Direction sideA, Direction sideB, ConnectionType type) {
        return null;
    }

    @Override
    public IConnection<IBundledDevice> createConnection(IBundledDevice a, IBundledDevice b, Direction sideA, Direction sideB, ConnectionType type) {
        return null;
    }

    @Override
    public IConnectionCache<IRedstoneDevice> createRedstoneConnectionCache(IRedstoneDevice dev) {
        return null;
    }

    @Override
    public IConnectionCache<IBundledDevice> createBundledConnectionCache(IBundledDevice dev) {
        return null;
    }

    @Override
    public IPropagator<IRedstoneDevice> getRedstonePropagator(IRedstoneDevice device, Direction side) {
        return null;
    }

    @Override
    public IPropagator<IBundledDevice> getBundledPropagator(IBundledDevice device, Direction side) {
        return null;
    }
}
