package com.bluepowermod.api.bluestone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.vec.Vec3i;

public interface IBluestoneApi {

    IBluestoneDevice getDevice(World world, int x, int y, int z);

    IBluestoneDevice getDevice(World world, int x, int y, int z, ForgeDirection face);

    IBluestoneDevice getDevice(World world, Vec3i location);

    IBluestoneDevice getDevice(World world, Vec3i location, ForgeDirection face);

}
