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

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.helper.ConnectionLogicHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedConnectionHelper {

    private static ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection> redstone = new ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection>(
            new ConnectionLogicHelper.IConnectableProvider<IRedstoneDevice, RedstoneConnection>() {

                @Override
                public IRedstoneDevice getConnectableAt(Level world, BlockPos pos, Direction face, Direction side) {

                    return RedstoneApi.getInstance().getRedstoneDevice(world, pos, face, side);
                }

                @Override
                public RedstoneConnection createConnection(IRedstoneDevice a, IRedstoneDevice b, Direction sideA, Direction sideB,
                                                           ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IRedstoneDevice from, IRedstoneDevice to, Direction side, ConnectionType type) {

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
                public boolean isNormalFace(IRedstoneDevice o, Direction face) {

                    return o.isNormalFace(face);
                }
            });

    private static ConnectionLogicHelper<IBundledDevice, BundledConnection> bundled = new ConnectionLogicHelper<IBundledDevice, BundledConnection>(
            new ConnectionLogicHelper.IConnectableProvider<IBundledDevice, BundledConnection>() {

                @Override
                public IBundledDevice getConnectableAt(Level world, BlockPos pos, Direction face, Direction side) {

                    return RedstoneApi.getInstance().getBundledDevice(world, pos, face, side);
                }

                @Override
                public BundledConnection createConnection(IBundledDevice a, IBundledDevice b, Direction sideA, Direction sideB,
                                                          ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IBundledDevice from, IBundledDevice to, Direction side, ConnectionType type) {

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
                public boolean isNormalFace(IBundledDevice o, Direction face) {

                    return o.isNormalFace(face);
                }
            });

    public static RedstoneConnection getNeighbor(IRedstoneDevice device, Direction side) {

        return redstone.getNeighbor(device, side);
    }

    public static BundledConnection getBundledNeighbor(IBundledDevice device, Direction side) {

        return bundled.getNeighbor(device, side);
    }
}