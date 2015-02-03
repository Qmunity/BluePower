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

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.redstone.BundledConnection;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnection;

public class WireHelper {

    public static RedstoneConnection getNeighbor(IRedstoneDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // FIXME Neighbor calculation

        // Straight connection
        do {
            Vec3i loc = new Vec3i(device).add(side);
            IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
                    side.getOpposite());

            if (dev == null)
                break;

            if (device.canConnect(side, dev, ConnectionType.STRAIGHT)
                    && dev.canConnect(side.getOpposite(), device, ConnectionType.STRAIGHT))
                return RedstoneApi.getInstance().createConnection(device, dev, side, side.getOpposite(), ConnectionType.STRAIGHT);
        } while (false);

        // // In same block
        // do {
        // Vec3i loc = new Vec3i(device);
        // ForgeDirection devFace = side == face.getOpposite() ? ForgeDirection.UNKNOWN : side;
        // IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
        // devFace, face);
        // if (dev != null && dev != device) {
        // if (face != ForgeDirection.UNKNOWN) {
        // if (dev instanceof IFaceRedstoneDevice) {
        // IFaceRedstoneDevice d1 = (IFaceRedstoneDevice) device;
        // IFaceRedstoneDevice d2 = (IFaceRedstoneDevice) dev;
        // if (d1.canConnectClosedCorner(side, d2) && d2.canConnectClosedCorner(face, d1))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, face);
        // } else {
        // if (device.canConnectStraight(side, dev) && dev.canConnectStraight(face, device))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, face);
        // }
        // } else {
        // if (dev instanceof IFaceRedstoneDevice) {
        // if (device.canConnectStraight(side, dev) && dev.canConnectStraight(face, device))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, face);
        // } else {
        // if (device.canConnectStraight(side, dev) && dev.canConnectStraight(side.getOpposite(), device))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, side.getOpposite());
        // }
        // }
        // }
        // } while (false);
        //
        // // On same block
        // do {
        // Vec3i loc = new Vec3i(device).add(face).add(side);
        // IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
        // side.getOpposite(), face.getOpposite());
        // if (dev != null && dev != device) {
        // if (device.canConnectOpenCorner(side, dev) && dev.canConnectOpenCorner(face.getOpposite(), device)) {
        // // Check occlusion on the corner block
        // Vec3i block = new Vec3i(device).add(side);
        // // Full block check
        // if (block.getBlock().isNormalCube(block.getWorld(), block.getX(), block.getY(), block.getZ()))
        // break;
        // // Microblock check
        // if (OcclusionHelper.microblockOcclusionTest(block, MicroblockShape.EDGE, 1, face, side.getOpposite()))
        // break;
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, face.getOpposite());
        // }
        // }
        // } while (false);
        //
        // // Straight connection
        // do {
        // Vec3i loc = new Vec3i(device).add(side);
        // IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
        // side.getOpposite());
        // if (dev != null && dev != device) {
        // if (device.canConnectStraight(side, dev) && dev.canConnectStraight(side.getOpposite(), device))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, side.getOpposite());
        // } else {
        // dev = RedstoneApi.getInstance().getRedstoneDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
        // ForgeDirection.UNKNOWN, side.getOpposite());
        // if (dev != null && dev != device && (device.isNormalBlock() || dev.isNormalBlock())) {
        // if (device.canConnectStraight(side, dev) && dev.canConnectStraight(side.getOpposite(), device))
        // return new Pair<IRedstoneDevice, ForgeDirection>(dev, side.getOpposite());
        // }
        // }
        // } while (false);

        return null;
    }

    public static BundledConnection getBundledNeighbor(IBundledDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // FIXME Neighbor calculation

        // Straight connection
        do {
            Vec3i loc = new Vec3i(device).add(side);
            IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
                    side.getOpposite());

            if (dev == null)
                break;

            if (device.canConnect(side, dev, ConnectionType.STRAIGHT)
                    && dev.canConnect(side.getOpposite(), device, ConnectionType.STRAIGHT))
                return RedstoneApi.getInstance().createConnection(device, dev, side, side.getOpposite(), ConnectionType.STRAIGHT);
        } while (false);

        // // In same block
        // do {
        // Vec3i loc = new Vec3i(device);
        // ForgeDirection devFace = side == face.getOpposite() ? ForgeDirection.UNKNOWN : side;
        // IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), devFace,
        // face);
        // if (dev != null && dev != device) {
        // if (face != ForgeDirection.UNKNOWN) {
        // if (dev instanceof IFaceRedstoneDevice) {
        // IFaceBundledDevice d1 = (IFaceBundledDevice) device;
        // IFaceBundledDevice d2 = (IFaceBundledDevice) dev;
        // if (d1.canConnectBundledClosedCorner(side, d2) && d2.canConnectBundledClosedCorner(face, d1))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, face);
        // } else {
        // if (device.canConnectBundledStraight(side, dev) && dev.canConnectBundledStraight(face, device))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, face);
        // }
        // } else {
        // if (dev instanceof IFaceBundledDevice) {
        // if (device.canConnectBundledStraight(side, dev) && dev.canConnectBundledStraight(face, device))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, face);
        // } else {
        // if (device.canConnectBundledStraight(side, dev) && dev.canConnectBundledStraight(side.getOpposite(), device))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, side.getOpposite());
        // }
        // }
        // }
        // } while (false);
        //
        // // On same block
        // do {
        // Vec3i loc = new Vec3i(device).add(face).add(side);
        // IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
        // side.getOpposite(), face.getOpposite());
        // if (dev != null && dev != device) {
        // if (device.canConnectBundledOpenCorner(side, dev) && dev.canConnectBundledOpenCorner(face.getOpposite(), device)) {
        // // Check occlusion on the corner block
        // Vec3i block = new Vec3i(device).add(side);
        // // Full block check
        // if (block.getBlock().isNormalCube(block.getWorld(), block.getX(), block.getY(), block.getZ()))
        // break;
        // // Microblock check
        // if (OcclusionHelper.microblockOcclusionTest(block, MicroblockShape.EDGE, 1, face, side.getOpposite()))
        // break;
        // return new Pair<IBundledDevice, ForgeDirection>(dev, face.getOpposite());
        // }
        // }
        // } while (false);
        //
        // // Straight connection
        // do {
        // Vec3i loc = new Vec3i(device).add(side);
        // IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
        // side.getOpposite());
        // if (dev != null && dev != device) {
        // if (device.canConnectBundledStraight(side, dev) && dev.canConnectBundledStraight(side.getOpposite(), device))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, side.getOpposite());
        // } else {
        // dev = RedstoneApi.getInstance().getBundledDevice(device.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
        // ForgeDirection.UNKNOWN, side.getOpposite());
        // if (dev != null && dev != device && (device.isNormalBlock() || dev.isNormalBlock())) {
        // if (device.canConnectBundledStraight(side, dev) && dev.canConnectBundledStraight(side.getOpposite(), device))
        // return new Pair<IBundledDevice, ForgeDirection>(dev, side.getOpposite());
        // }
        // }
        // } while (false);

        return null;
    }
}
