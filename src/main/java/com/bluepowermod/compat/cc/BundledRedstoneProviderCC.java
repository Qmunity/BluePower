package com.bluepowermod.compat.cc;

import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;

import dan200.computercraft.api.redstone.IBundledRedstoneProvider;

public class BundledRedstoneProviderCC implements IBundledRedstoneProvider {

    @Override
    public int getBundledRedstoneOutput(World world, int x, int y, int z, int side) {

        BlockPos v = new BlockPos(x, y, z).add(EnumFacing.getFront(side));

        return CCUtils.packDigital(BundledDeviceCCComputer.getDeviceAt(world, v.getX(), v.getY(), v.getZ()).getCurPow(
                EnumFacing.getFront(side).getOpposite()));
    }

}
