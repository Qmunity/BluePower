package com.bluepowermod.compat.cc;

import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

;

public class BundledRedstoneProviderCC implements IBundledRedstoneProvider {

    @Override
    public int getBundledRedstoneOutput(World world, BlockPos pos, EnumFacing side) {

        BlockPos v = pos.offset(side);

        return CCUtils.packDigital(BundledDeviceCCComputer.getDeviceAt(world, v).getCurPow(
                side.getOpposite()));
    }

}
