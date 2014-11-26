package com.bluepowermod.part.wire;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class WireHelper {

    public static IRedstoneDevice getNeighbour(IRedstoneDevice device, ForgeDirection side) {

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

                return dev;
            }
        }

        return null;
    }
}
