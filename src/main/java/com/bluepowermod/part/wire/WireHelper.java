package com.bluepowermod.part.wire;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class WireHelper {

    public static Entry<IRedstoneDevice, ForgeDirection> getNeighbor(IRedstoneDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFaceRedstoneDevice)
            face = ((IFace) device).getFace();

        // Straight connection
        {
            Vec3i loc = new Vec3i(device).add(side);
            for (int i = -1; i < 6; i++) {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d,
                        side.getOpposite());
                if (dev == null)
                    continue;

                // If our device isn't placed on a face and the other one is, if this isn't a normal block, continue.
                if (face == ForgeDirection.UNKNOWN && d != ForgeDirection.UNKNOWN && !device.isNormalBlock())
                    continue;
                // And vice versa.
                if (d == ForgeDirection.UNKNOWN && face != ForgeDirection.UNKNOWN && !dev.isNormalBlock())
                    continue;
                // If they're both on the face of a block and that face doesn't match, continue.
                if (face != ForgeDirection.UNKNOWN && d != ForgeDirection.UNKNOWN && face != d)
                    continue;
                // If one of them can't connect to the other, continue.
                if (!device.canConnectStraight(dev) || !dev.canConnectStraight(device))
                    continue;

                return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        // Same block
        {
            Vec3i loc = new Vec3i(device);
            IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side == face.getOpposite() ? ForgeDirection.UNKNOWN : side, side.getOpposite());
            if (dev != null && dev != device)
                if (device.canConnectStraight(dev) && dev.canConnectStraight(device))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
        }

        return null;
    }

    public static Entry<IBundledDevice, ForgeDirection> getBundledNeighbor(IBundledDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFaceBundledDevice)
            face = ((IFace) device).getFace();

        // Straight connection
        {
            Vec3i loc = new Vec3i(device).add(side);
            for (int i = -1; i < 6; i++) {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d,
                        side.getOpposite());
                if (dev == null)
                    continue;

                // If our device isn't placed on a face and the other one is, if this isn't a normal block, continue.
                if (face == ForgeDirection.UNKNOWN && d != ForgeDirection.UNKNOWN && !device.isNormalBlock())
                    continue;
                // And vice versa.
                if (d == ForgeDirection.UNKNOWN && face != ForgeDirection.UNKNOWN && !dev.isNormalBlock())
                    continue;
                // If they're both on the face of a block and that face doesn't match, continue.
                if (face != ForgeDirection.UNKNOWN && d != ForgeDirection.UNKNOWN && face != d)
                    continue;
                // If one of them can't connect to the other, continue.
                if (!device.canConnectBundledStraight(dev) || !dev.canConnectBundledStraight(device))
                    continue;

                return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        return null;
    }

    public static ForgeDirection getConnectionSide(IRedstoneDevice device, IRedstoneDevice device2) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (device.getDeviceOnSide(d) == device2)
                return d;

        return ForgeDirection.UNKNOWN;
    }
}
