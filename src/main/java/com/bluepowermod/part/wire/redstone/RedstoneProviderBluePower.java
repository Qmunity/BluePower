package com.bluepowermod.part.wire.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;

public class RedstoneProviderBluePower implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null) {
            for (IPart p : holder.getParts()) {
                if (p instanceof IRedstoneDevice) {
                    if (p instanceof IFaceRedstoneDevice) {
                        if (((IFaceRedstoneDevice) p).getFace() == face)
                            return (IRedstoneDevice) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN)
                            return (IRedstoneDevice) p;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null) {
            for (IPart p : holder.getParts()) {
                if (p instanceof IBundledDevice) {
                    if (p instanceof IFaceBundledDevice) {
                        if (((IFaceBundledDevice) p).getFace() == face)
                            return (IBundledDevice) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN)
                            return (IBundledDevice) p;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return null;
    }

}
