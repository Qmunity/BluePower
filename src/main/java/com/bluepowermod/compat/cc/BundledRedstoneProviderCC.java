package com.bluepowermod.compat.cc;

import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.vec.Vec3i;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;

public class BundledRedstoneProviderCC implements IBundledRedstoneProvider {

    @Override
    public int getBundledRedstoneOutput(World world, int x, int y, int z, int side) {

        Vec3i v = new Vec3i(x, y, z).add(EnumFacing.getOrientation(side));

        return CCUtils.packDigital(BundledDeviceCCComputer.getDeviceAt(world, v.getX(), v.getY(), v.getZ()).getCurPow(
                EnumFacing.getOrientation(side).getOpposite()));
    }

}
