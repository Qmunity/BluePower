package com.bluepowermod.compat.cc;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;

public class RedstoneProviderCC implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        Block b = world.getBlock(x, y, z);
        if (b == null || face == ForgeDirection.UNKNOWN || side == ForgeDirection.UNKNOWN)
            return null;

        if (b.getClass().getName().startsWith("dan200.computercraft"))
            return BundledDeviceCCComputer.getDeviceAt(world, x, y, z);

        return null;
    }

}
