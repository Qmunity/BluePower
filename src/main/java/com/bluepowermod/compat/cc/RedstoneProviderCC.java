package com.bluepowermod.compat.cc;
/*
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;

public class RedstoneProviderCC implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(Level world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(Level world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        Block b = world.getBlock(x, y, z);
        if (b == null || face == null || side == null)
            return null;

        if (b.getClass().getName().startsWith("dan200.computercraft"))
            return BundledDeviceCCComputer.getDeviceAt(world, x, y, z);

        return null;
    }

}*/
