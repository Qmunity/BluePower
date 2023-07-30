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

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedstoneProviderVanilla implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(Level world, BlockPos pos, Direction face, Direction side) {

        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof IRedstoneDevice)
            return (IRedstoneDevice) te;

        return DummyRedstoneDevice.getDeviceAt(world, pos);
    }

    @Override
    public IBundledDevice getBundledDeviceAt(Level world, BlockPos pos, Direction face, Direction side) {

        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof IBundledDevice)
            return (IBundledDevice) te;

        return null;
    }

}