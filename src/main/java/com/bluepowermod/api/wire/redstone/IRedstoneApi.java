package com.bluepowermod.api.wire.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;

public interface IRedstoneApi {

    /**
     * Returns the redstone device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IRedstoneProvider}s
     *
     * @param world
     *            The world where the device is
     * @param x
     *            X coordinate of the device
     * @param y
     *            Y coordinate of the device
     * @param z
     *            Z coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link ForgeDirection#UNKNOWN} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IRedstoneProvider}s
     *
     * @param world
     *            The world where the device is
     * @param x
     *            X coordinate of the device
     * @param y
     *            Y coordinate of the device
     * @param z
     *            Z coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link ForgeDirection#UNKNOWN} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

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

    public IConnection<IRedstoneDevice> createConnection(IRedstoneDevice a, IRedstoneDevice b, ForgeDirection sideA, ForgeDirection sideB,
            ConnectionType type);

    public IConnection<IBundledDevice> createConnection(IBundledDevice a, IBundledDevice b, ForgeDirection sideA, ForgeDirection sideB,
            ConnectionType type);

    public IConnectionCache<IRedstoneDevice> createRedstoneConnectionCache(IRedstoneDevice dev);

    public IConnectionCache<IBundledDevice> createBundledConnectionCache(IBundledDevice dev);

    public IPropagator<IRedstoneDevice> getRedstonePropagator(IRedstoneDevice device, ForgeDirection side);

    public IPropagator<IBundledDevice> getBundledPropagator(IBundledDevice device, ForgeDirection side);

}
