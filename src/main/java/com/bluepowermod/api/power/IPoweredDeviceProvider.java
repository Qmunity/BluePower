package com.bluepowermod.api.power;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPoweredDeviceProvider {

    /**
     * Returns the powered device at the specified coordinates and on the specified side and face.
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
     * @return The powered device at the specified coords, side and face.
     */
    public IPowered getPoweredDeviceAt(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face);

}
