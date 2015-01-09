package com.bluepowermod.compat.cc;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;

public class RedstoneProviderCC implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        Block b = world.getBlock(x, y, z);
        if (b == null || face == ForgeDirection.UNKNOWN || side == ForgeDirection.UNKNOWN)
            return null;

        if (b.getClass().getName().startsWith("dan200.computercraft"))
            return BundledDeviceCCComputer.getDeviceAt(world, x, y, z);

        return null;
    }

}
