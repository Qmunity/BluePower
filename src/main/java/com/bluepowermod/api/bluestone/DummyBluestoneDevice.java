package com.bluepowermod.api.bluestone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.vec.Vec3i;

public class DummyBluestoneDevice implements IBluestoneDevice {

    private static List<DummyBluestoneDevice> devices = new ArrayList<DummyBluestoneDevice>();

    public static DummyBluestoneDevice getDeviceAt(World world, int x, int y, int z) {

        DummyBluestoneDevice device = null;
        Vec3i loc = new Vec3i(x, y, z, world);

        for (DummyBluestoneDevice d : devices) {
            if (d.location.equals(loc)) {
                device = d;
                break;
            }
        }

        if (device != null) {
            if (device.location.getBlock() != loc.getBlock() || device.location.getBlockMeta() != loc.getBlockMeta()
                    || device.location.getTileEntity() != loc.getTileEntity()) {
                devices.remove(device);
                devices.add(device = new DummyBluestoneDevice(loc.getWorld(), loc));
            }
        } else {
            devices.add(device = new DummyBluestoneDevice(loc.getWorld(), loc));
        }

        return device;
    }

    private Vec3i location;
    private List<IBluestoneHandler> handlers = new ArrayList<IBluestoneHandler>();

    public DummyBluestoneDevice(World world, Vec3i location) {

        this.location = location.setWorld(world).getImmutableCopy();

        handlers.add(new DummyBluestonehandler(this, BluestoneColor.NONE, 0x012345));
    }

    public DummyBluestoneDevice(World world, int x, int y, int z) {

        this(world, new Vec3i(x, y, z));
    }

    public DummyBluestoneDevice() {

    }

    @Override
    public World getWorld() {

        return location.getWorld();
    }

    @Override
    public int getX() {

        return location.getX();
    }

    @Override
    public int getY() {

        return location.getY();
    }

    @Override
    public int getZ() {

        return location.getZ();
    }

    @Override
    public BluestoneColor getBundleColor() {

        return BluestoneColor.INVALID;
    }

    @Override
    public List<IBluestoneHandler> getHandlers() {

        return handlers;
    }

    @Override
    public IBluestoneDevice getNeighbor(ForgeDirection side) {

        if (getWorld() == null)
            return null;

        return BPApi.getInstance().getBluestoneApi()
                .getDevice(getWorld(), getX() + side.offsetX, getY() + side.offsetY, getZ() + side.offsetZ, true);
    }

    private static class DummyBluestonehandler extends DefaultBluestoneHandler {

        public DummyBluestonehandler(IBluestoneDevice parent, BluestoneColor color, int conductionMap) {

            super(parent, color, conductionMap);
        }

        @Override
        public boolean canConnect(IBluestoneDevice device) {

            if (!super.canConnect(device))
                return false;

            ForgeDirection side = new Vec3i(device).getDirectionTo(new Vec3i(getDevice()));

            if (side == ForgeDirection.UNKNOWN)
                return true;

            if (device instanceof IPartFace)
                return RedstoneHelper.canConnect(getDevice().getWorld(), getDevice().getX(), getDevice().getY(), getDevice().getZ(),
                        side.getOpposite(), ((IPartFace) device).getFace());
            return RedstoneHelper.canConnect(getDevice().getWorld(), getDevice().getX(), getDevice().getY(), getDevice().getZ(),
                    side.getOpposite());
        }

    }

}
