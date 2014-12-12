/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.wire.redstone;

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
                if (!device.canConnectStraight(side, dev) || !dev.canConnectStraight(side.getOpposite(), device))
                    continue;

                return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        // In same block
        {
            Vec3i loc = new Vec3i(device);
            IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side == face.getOpposite() ? ForgeDirection.UNKNOWN : side, side.getOpposite());
            if (dev != null && dev != device && (side == face.getOpposite() || dev instanceof IFaceRedstoneDevice)) {
                if (device instanceof IFaceRedstoneDevice && dev instanceof IFaceRedstoneDevice
                        && ((IFaceRedstoneDevice) device).canConnectClosedCorner(side, dev)
                        && ((IFaceRedstoneDevice) dev).canConnectClosedCorner(side.getOpposite(), device))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
                if (device instanceof IFaceRedstoneDevice && !(dev instanceof IFaceRedstoneDevice)
                        && device.canConnectStraight(ForgeDirection.UNKNOWN, dev) && dev.canConnectStraight(face, device))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
                if (dev instanceof IFaceRedstoneDevice && !(device instanceof IFaceRedstoneDevice)
                        && dev.canConnectStraight(ForgeDirection.UNKNOWN, device) && device.canConnectStraight(face, dev))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        // On same block
        {
            Vec3i loc = new Vec3i(device).add(face).add(side);
            IRedstoneDevice faceDev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side.getOpposite(), face.getOpposite());
            if (faceDev != null) {
                if (device.canConnectOpenCorner(side, faceDev) && faceDev.canConnectOpenCorner(face.getOpposite(), device))
                    return new AbstractMap.SimpleEntry(faceDev, face.getOpposite());
            } else {
                IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                        ForgeDirection.UNKNOWN, face.getOpposite());
                if (dev != null && dev.isNormalBlock())
                    if (device.canConnectOpenCorner(side, faceDev) && dev.canConnectOpenCorner(face.getOpposite(), device))
                        return new AbstractMap.SimpleEntry(faceDev, face.getOpposite());
            }
        }

        return null;
    }

    public static Entry<IBundledDevice, ForgeDirection> getBundledNeighbor(IBundledDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFaceBundledDevice)
            face = ((IFace) device).getFace();

        if (!device.isBundled())
            return null;

        // Straight connection
        {
            Vec3i loc = new Vec3i(device).add(side);
            for (int i = -1; i < 6; i++) {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d,
                        side.getOpposite());
                if (dev == null)
                    continue;
                if (!dev.isBundled())
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
                if (!device.canConnectBundledStraight(side, dev) || !dev.canConnectBundledStraight(side.getOpposite(), device))
                    continue;

                return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        // In same block
        {
            Vec3i loc = new Vec3i(device);
            IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side == face.getOpposite() ? ForgeDirection.UNKNOWN : side, side.getOpposite());
            if (dev != null && dev != device && dev.isBundled() && (side == face.getOpposite() || dev instanceof IFaceBundledDevice)) {
                if (device instanceof IFaceBundledDevice && dev instanceof IFaceBundledDevice
                        && ((IFaceBundledDevice) device).canConnectBundledClosedCorner(side, dev)
                        && ((IFaceBundledDevice) dev).canConnectBundledClosedCorner(side.getOpposite(), device))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
                if (device instanceof IFaceBundledDevice && !(dev instanceof IFaceBundledDevice)
                        && device.canConnectBundledStraight(ForgeDirection.UNKNOWN, dev) && dev.canConnectBundledStraight(face, device))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
                if (dev instanceof IFaceBundledDevice && !(device instanceof IFaceBundledDevice)
                        && dev.canConnectBundledStraight(ForgeDirection.UNKNOWN, device) && device.canConnectBundledStraight(face, dev))
                    return new AbstractMap.SimpleEntry(dev, side.getOpposite());
            }
        }

        // On same block
        {
            Vec3i loc = new Vec3i(device).add(face).add(side);
            IBundledDevice faceDev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side.getOpposite(), face.getOpposite());
            if (faceDev != null) {
                if (device.canConnectBundledOpenCorner(side, faceDev) && faceDev.canConnectBundledOpenCorner(face.getOpposite(), device)
                        && faceDev.isBundled())
                    return new AbstractMap.SimpleEntry(faceDev, face.getOpposite());
            } else {
                IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                        ForgeDirection.UNKNOWN, face.getOpposite());
                if (dev != null && dev.isNormalBlock() && dev.isBundled())
                    if (device.canConnectBundledOpenCorner(side, faceDev) && dev.canConnectBundledOpenCorner(face.getOpposite(), device))
                        return new AbstractMap.SimpleEntry(faceDev, face.getOpposite());
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

    public static ForgeDirection getConnectionSide(IBundledDevice device, IBundledDevice device2) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (device.getBundledDeviceOnSide(d) == device2)
                return d;

        return ForgeDirection.UNKNOWN;
    }
}
