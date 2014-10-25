package com.bluepowermod.api.bluestone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBluestoneApi {

    IBluestoneDevice getDevice(World world, int x, int y, int z);

    IBluestoneDevice getDevice(World world, int x, int y, int z, ForgeDirection face);

    IBluestoneDevice getDevice(World world, int x, int y, int z, boolean includeRedstoneDevices);

    IBluestoneDevice getDevice(World world, int x, int y, int z, ForgeDirection face, boolean includeRedstoneDevices);

    IBluestoneHandler createDefaultBluestoneHandler(IBluestoneDevice parent, BluestoneColor color, int conductionMap);

    IBluestoneHandler createDefaultBluestoneHandler(IBluestoneDevice parent, BluestoneColor color);

}
