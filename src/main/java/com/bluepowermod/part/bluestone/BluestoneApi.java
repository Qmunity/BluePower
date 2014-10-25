package com.bluepowermod.part.bluestone;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.DummyBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneHandler;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.compat.MultipartCompatibility;
import com.qmunity.lib.vec.Vec3i;

public class BluestoneApi implements IBluestoneApi {

    private static BluestoneApi INSTANCE = new BluestoneApi();

    private BluestoneApi() {

    }

    public static BluestoneApi getInstance() {

        return INSTANCE;
    }

    @Override
    public IBluestoneDevice getDevice(World world, int x, int y, int z) {

        return getDevice(world, new Vec3i(x, y, z), ForgeDirection.UNKNOWN, false);
    }

    @Override
    public IBluestoneDevice getDevice(World world, int x, int y, int z, ForgeDirection face) {

        return getDevice(world, new Vec3i(x, y, z), face, false);
    }

    private IBluestoneDevice getDevice(World world, Vec3i location, ForgeDirection face, boolean includeRedstoneDevices) {

        if (face == null)
            return null;

        location = location.clone().setWorld(world);

        ITilePartHolder partHolder = MultipartCompatibility.getPartHolder(world, location.getX(), location.getY(), location.getZ());
        if (partHolder != null) {
            for (IPart p : partHolder.getParts()) {
                if (p instanceof IBluestoneDevice) {
                    if (p instanceof IPartFace) {
                        if (face != ForgeDirection.UNKNOWN && ((IPartFace) p).getFace() == face)
                            return (IBluestoneDevice) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN)
                            return (IBluestoneDevice) p;
                    }
                }
            }

            return null;
        }

        TileEntity te = location.getTileEntity();
        if (te != null && te instanceof IBluestoneDevice)
            return (IBluestoneDevice) te;

        if (includeRedstoneDevices) {
            Block b = location.getBlock();
            boolean can = false;
            for (int i = -1; i < 4; i++) {
                if (b.canConnectRedstone(world, location.getX(), location.getY(), location.getZ(), i)) {
                    can = true;
                    break;
                }
            }
            if (can)
                return DummyBluestoneDevice.getDeviceAt(world, location.getX(), location.getY(), location.getZ());
        }

        return null;
    }

    @Override
    public IBluestoneDevice getDevice(World world, int x, int y, int z, boolean includeRedstoneDevices) {

        return getDevice(world, new Vec3i(x, y, z), ForgeDirection.UNKNOWN, includeRedstoneDevices);
    }

    @Override
    public IBluestoneDevice getDevice(World world, int x, int y, int z, ForgeDirection face, boolean includeRedstoneDevices) {

        return getDevice(world, new Vec3i(x, y, z), face, includeRedstoneDevices);
    }

    @Override
    public IBluestoneHandler createDefaultBluestoneHandler(IBluestoneDevice parent, BluestoneColor color, int conductionMap) {

        return new DefaultBluestoneHandler(parent, color, conductionMap);
    }

    @Override
    public IBluestoneHandler createDefaultBluestoneHandler(IBluestoneDevice parent, BluestoneColor color) {

        return createDefaultBluestoneHandler(parent, color, 0x012345);
    }

}
