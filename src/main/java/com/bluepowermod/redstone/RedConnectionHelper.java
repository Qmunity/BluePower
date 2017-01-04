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
import com.bluepowermod.part.wire.ConnectionLogicHelper;
import com.bluepowermod.part.wire.ConnectionLogicHelper.IConnectableProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

;

public class RedConnectionHelper {

    private static ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection> redstone = new ConnectionLogicHelper<IRedstoneDevice, RedstoneConnection>(
            new IConnectableProvider<IRedstoneDevice, RedstoneConnection>() {

                @Override
                public IRedstoneDevice getConnectableAt(World world, BlockPos pos, EnumFacing side) {
                    return RedstoneApi.getInstance().getRedstoneDevice(world, pos, side, null);
                }

                @Override
                public IRedstoneDevice getConnectableAt(World world, BlockPos pos,  EnumFacing side, EnumFacing face) {

                    return RedstoneApi.getInstance().getRedstoneDevice(world, pos, side, face);
                }

                @Override
                public RedstoneConnection createConnection(IRedstoneDevice a, IRedstoneDevice b, EnumFacing sideA, EnumFacing sideB,
                        ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IRedstoneDevice from, IRedstoneDevice to, EnumFacing side, ConnectionType type) {

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
                public boolean isNormalFace(IRedstoneDevice o, EnumFacing face) {

                    return o.isNormalFace(face);
                }
            });

    private static ConnectionLogicHelper<IBundledDevice, BundledConnection> bundled = new ConnectionLogicHelper<IBundledDevice, BundledConnection>(
            new IConnectableProvider<IBundledDevice, BundledConnection>() {

                @Override
                public IBundledDevice getConnectableAt(World world, BlockPos pos, EnumFacing side) {

                    return RedstoneApi.getInstance().getBundledDevice(world, pos, null, side);
                }

                @Override
                public IBundledDevice getConnectableAt(World world, BlockPos pos, EnumFacing face, EnumFacing side) {

                    return RedstoneApi.getInstance().getBundledDevice(world, pos, face, side);
                }

                @Override
                public BundledConnection createConnection(IBundledDevice a, IBundledDevice b, EnumFacing sideA, EnumFacing sideB,
                        ConnectionType type) {

                    return RedstoneApi.getInstance().createConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IBundledDevice from, IBundledDevice to, EnumFacing side, ConnectionType type) {

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
                public boolean isNormalFace(IBundledDevice o, EnumFacing face) {

                    return o.isNormalFace(face);
                }
            });

    public static RedstoneConnection getNeighbor(IRedstoneDevice device, EnumFacing side) {

        return redstone.getNeighbor(device, side);
    }

    public static BundledConnection getBundledNeighbor(IBundledDevice device, EnumFacing side) {

        return bundled.getNeighbor(device, side);
    }
}
