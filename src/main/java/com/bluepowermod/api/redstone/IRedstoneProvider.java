package com.bluepowermod.api.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Allows you to make wire connect to blocks that don't already have an implementation of the wire API. (For example, computers from CC)
 *
 * @author amadornes
 */
public interface IRedstoneProvider {

    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face);

}