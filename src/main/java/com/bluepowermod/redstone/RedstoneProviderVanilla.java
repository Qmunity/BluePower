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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

;

public class RedstoneProviderVanilla implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, BlockPos pos, EnumFacing face, EnumFacing side) {

        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof IRedstoneDevice)
            return (IRedstoneDevice) te;

        return DummyRedstoneDevice.getDeviceAt(pos, world, world.getBlockState(pos).getBlock());
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, BlockPos pos, EnumFacing face, EnumFacing side) {

        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof IBundledDevice)
            return (IBundledDevice) te;

        return null;
    }

}
