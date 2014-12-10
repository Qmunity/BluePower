package com.bluepowermod.part.wire.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;

public class RedstoneProviderVanilla implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return new DummyRedstoneDevice(new Vec3i(x, y, z, world));
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return null;
    }

    @Override
    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return null;
    }

}
