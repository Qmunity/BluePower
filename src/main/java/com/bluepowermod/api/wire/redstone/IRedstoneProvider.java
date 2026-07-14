package com.bluepowermod.api.wire.redstone;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRedstoneProvider {

    /**
     * Returns the redstone device at the specified coordinates and on the specified side and face.
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Location of the device.
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The redstone device at the specified coords, side and face.
     */
    IRedstoneDevice getRedstoneDeviceAt(Level world, BlockPos pos, Direction face, Direction side);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face.
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Location of the device.
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The bundled device at the specified coords, side and face.
     */
    IBundledDevice getBundledDeviceAt(Level world, BlockPos pos, Direction face, Direction side);



}
