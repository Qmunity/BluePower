package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

;

public interface IRedstoneApi {

    /**
     * Returns the redstone device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IRedstoneProvider}s
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    public IRedstoneDevice getRedstoneDevice(Level world, BlockPos pos, Direction face, Direction side);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IRedstoneProvider}s
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    public IBundledDevice getBundledDevice(Level world, BlockPos pos, Direction face, Direction side);

    /**
     * Registers a redstone/bundled device provider.
     */
    public void registerRedstoneProvider(IRedstoneProvider provider);

    /**
     * Returns whether or not wires should output power.
     */
    public boolean shouldWiresOutputPower(boolean lossy);

    /**
     * Determines whether or not wires should output power.
     */
    public void setWiresOutputPower(boolean shouldWiresOutputPower, boolean lossy);

    /**
     * Returns whether or not wires should handle block/tile/part updates.
     */
    public boolean shouldWiresHandleUpdates();

    /**
     * Determines whether or not wires should handle block/tile/part updates.
     *
     * @param shouldWiresHandleUpdates
     */
    public void setWiresHandleUpdates(boolean shouldWiresHandleUpdates);

    public IConnection<IRedstoneDevice> createConnection(IRedstoneDevice a, IRedstoneDevice b, Direction sideA, Direction sideB,
                                                         ConnectionType type);

    public IConnection<IBundledDevice> createConnection(IBundledDevice a, IBundledDevice b, Direction sideA, Direction sideB,
                                                        ConnectionType type);

    public IConnectionCache<IRedstoneDevice> createRedstoneConnectionCache(IRedstoneDevice dev);

    public IConnectionCache<IBundledDevice> createBundledConnectionCache(IBundledDevice dev);

    public IPropagator<IRedstoneDevice> getRedstonePropagator(IRedstoneDevice device, Direction side);

    public IPropagator<IBundledDevice> getBundledPropagator(IBundledDevice device, Direction side);

}
