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

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.ConnectionLogicHelper;
import com.bluepowermod.part.wire.ConnectionLogicHelper.IConnectableProvider;

public class RedConnectionHelper {

    private static ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection> redstone = new ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection>(
            new IConnectableProvider<IRedstoneDevice, RedstoneConnection>() {

                @Override
                public IRedstoneDevice getConnectableAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

                    return RedstoneApi.getInstance().getRedstoneDevice(world, x, y, z, face, side);
                }

                @Override
                public IWorldLocation getLocation(IRedstoneDevice o) {

                    return o;
                }

                @Override
                public RedstoneConnection createConnection(IRedstoneDevice a, IRedstoneDevice b, ForgeDirection sideA, ForgeDirection sideB,
                        ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IRedstoneDevice from, IRedstoneDevice to, ForgeDirection side, ConnectionType type) {

                    return from.canConnect(side, to, type);
                }

                @Override
                public boolean isValidClosedCorner(IRedstoneDevice o) {

                    return !(o instanceof DummyRedstoneDevice);
                }

                @Override
                public boolean isValidOpenCorner(IRedstoneDevice o) {

                    return true;
                }

                @Override
                public boolean isValidStraight(IRedstoneDevice o) {

                    return true;
                }

                @Override
                public boolean isNormalFace(IRedstoneDevice o, ForgeDirection face) {

                    return o.isNormalFace(face);
                }
            });

    private static ConnectionLogicHelper<IBundledDevice, BundledConnection> bundled = new ConnectionLogicHelper<IBundledDevice, BundledConnection>(
            new IConnectableProvider<IBundledDevice, BundledConnection>() {

                @Override
                public IBundledDevice getConnectableAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

                    return RedstoneApi.getInstance().getBundledDevice(world, x, y, z, face, side);
                }

                @Override
                public IWorldLocation getLocation(IBundledDevice o) {

                    return o;
                }

                @Override
                public BundledConnection createConnection(IBundledDevice a, IBundledDevice b, ForgeDirection sideA, ForgeDirection sideB,
                        ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IBundledDevice from, IBundledDevice to, ForgeDirection side, ConnectionType type) {

                    return from.canConnect(side, to, type);
                }

                @Override
                public boolean isValidClosedCorner(IBundledDevice o) {

                    return !(o instanceof DummyRedstoneDevice);
                }

                @Override
                public boolean isValidOpenCorner(IBundledDevice o) {

                    return true;
                }

                @Override
                public boolean isValidStraight(IBundledDevice o) {

                    return true;
                }

                @Override
                public boolean isNormalFace(IBundledDevice o, ForgeDirection face) {

                    return o.isNormalFace(face);
                }
            });

    public static RedstoneConnection getNeighbor(IRedstoneDevice device, ForgeDirection side) {

        return redstone.getNeighbor(device, side);
    }

    public static BundledConnection getBundledNeighbor(IBundledDevice device, ForgeDirection side) {

        return bundled.getNeighbor(device, side);
    }
}
