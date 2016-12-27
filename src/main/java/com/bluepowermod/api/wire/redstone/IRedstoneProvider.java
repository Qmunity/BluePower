package com.bluepowermod.api.wire.redstone;

import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;

public interface IRedstoneProvider {

    /**
     * Returns the redstone device at the specified coordinates and on the specified side and face.
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
     *            Face the device must be placed on or {@link EnumFacing#UNKNOWN} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    public IRedstoneDevice getRedstoneDeviceAt(World world, int x, int y, int z, EnumFacing side, EnumFacing face);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face.
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
     *            Face the device must be placed on or {@link EnumFacing#UNKNOWN} if not know or not a face device
     * @return The bundled device at the specified coords, side and face.
     */
    public IBundledDevice getBundledDeviceAt(World world, int x, int y, int z, EnumFacing side, EnumFacing face);

}
