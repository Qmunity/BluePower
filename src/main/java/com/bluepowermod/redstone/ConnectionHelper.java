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

package com.bluepowermod.redstone;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public class ConnectionHelper {

    public static RedstoneConnection getNeighbor(IRedstoneDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // In same block
        do {
            Vec3i loc = new Vec3i(device);
            IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side == face.getOpposite() ? ForgeDirection.UNKNOWN : side, face == ForgeDirection.UNKNOWN ? side.getOpposite() : face);
            if (dev == null || dev == device || dev instanceof DummyRedstoneDevice)
                break;

            ConnectionType type = (device instanceof IFace || dev instanceof IFace) && !(device instanceof IFace == dev instanceof IFace) ? ConnectionType.STRAIGHT
                    : ConnectionType.CLOSED_CORNER;
            if (device.canConnect(side, dev, type) && dev.canConnect(face, device, type))
                return RedstoneApi.getInstance().createConnection(device, dev, side, face, type);
        } while (false);

        // On same block
        if (face != ForgeDirection.UNKNOWN) {
            do {
                Vec3i loc = new Vec3i(device).add(face).add(side);
                IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                        side.getOpposite(), face.getOpposite());
                if (dev == null || dev == device)
                    break;

                Vec3i block = new Vec3i(device).add(side);
                Block b = block.getBlock();
                // Full block check
                if (b.isNormalCube() || b == Blocks.redstone_block)
                    break;
                // Microblock check
                if (!OcclusionHelper.microblockOcclusionTest(block, MicroblockShape.EDGE, 1, face, side.getOpposite()))
                    break;

                if (device.canConnect(side, dev, ConnectionType.OPEN_CORNER)
                        && dev.canConnect(face.getOpposite(), device, ConnectionType.OPEN_CORNER))
                    return RedstoneApi.getInstance().createConnection(device, dev, side, face.getOpposite(), ConnectionType.OPEN_CORNER);
            } while (false);
        }

        // Straight connection
        do {
            Vec3i loc = new Vec3i(device).add(side);
            IRedstoneDevice dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
                    side.getOpposite());
            if (dev == null) {
                dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), side.getOpposite(),
                        side.getOpposite());
                if (dev == null && face == ForgeDirection.UNKNOWN && device.isNormalFace(side)) {
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        if (d != side && d != side.getOpposite()) {
                            dev = RedstoneApi.getInstance().getRedstoneDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d,
                                    side.getOpposite());
                            if (dev != null)
                                break;
                        }
                    }
                }
            }

            if (dev == null || dev == device)
                break;

            if (device.canConnect(side, dev, ConnectionType.STRAIGHT)
                    && dev.canConnect(side.getOpposite(), device, ConnectionType.STRAIGHT))
                return RedstoneApi.getInstance().createConnection(device, dev, side, side.getOpposite(), ConnectionType.STRAIGHT);
        } while (false);

        return null;
    }

    public static BundledConnection getBundledNeighbor(IBundledDevice device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // In same block
        do {
            Vec3i loc = new Vec3i(device);
            IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side == face.getOpposite() ? ForgeDirection.UNKNOWN : side, face == ForgeDirection.UNKNOWN ? side.getOpposite() : face);
            if (dev == null || dev == device || dev instanceof DummyRedstoneDevice)
                break;

            ConnectionType type = (device instanceof IFace || dev instanceof IFace) && !(device instanceof IFace == dev instanceof IFace) ? ConnectionType.STRAIGHT
                    : ConnectionType.CLOSED_CORNER;
            if (device.canConnect(side, dev, type) && dev.canConnect(face, device, type))
                return RedstoneApi.getInstance().createConnection(device, dev, side, face, type);
        } while (false);

        // On same block
        if (face != ForgeDirection.UNKNOWN) {
            do {
                Vec3i loc = new Vec3i(device).add(face).add(side);
                IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                        side.getOpposite(), face.getOpposite());
                if (dev == null || dev == device)
                    break;

                Vec3i block = new Vec3i(device).add(side);
                Block b = block.getBlock();
                // Full block check
                if (b.isNormalCube() || b == Blocks.redstone_block)
                    break;
                // Microblock check
                if (!OcclusionHelper.microblockOcclusionTest(block, MicroblockShape.EDGE, 1, face, side.getOpposite()))
                    break;

                if (device.canConnect(side, dev, ConnectionType.OPEN_CORNER)
                        && dev.canConnect(face.getOpposite(), device, ConnectionType.OPEN_CORNER))
                    return RedstoneApi.getInstance().createConnection(device, dev, side, face.getOpposite(), ConnectionType.OPEN_CORNER);
            } while (false);
        }

        // Straight connection
        do {
            Vec3i loc = new Vec3i(device).add(side);
            IBundledDevice dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face,
                    side.getOpposite());
            if (dev == null) {
                dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), side.getOpposite(),
                        side.getOpposite());
                if (dev == null && face == ForgeDirection.UNKNOWN && device.isNormalFace(side)) {
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        if (d != side && d != side.getOpposite()) {
                            dev = RedstoneApi.getInstance().getBundledDevice(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d,
                                    side.getOpposite());
                            if (dev != null)
                                break;
                        }
                    }
                }
            }

            if (dev == null || dev == device)
                break;

            if (device.canConnect(side, dev, ConnectionType.STRAIGHT)
                    && dev.canConnect(side.getOpposite(), device, ConnectionType.STRAIGHT))
                return RedstoneApi.getInstance().createConnection(device, dev, side, side.getOpposite(), ConnectionType.STRAIGHT);
        } while (false);

        return null;
    }
}
