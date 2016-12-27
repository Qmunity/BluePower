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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;

public class RedstoneProviderVanilla implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IRedstoneDevice)
            return (IRedstoneDevice) te;

        return DummyRedstoneDevice.getDeviceAt(new Vec3i(x, y, z, world));
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IBundledDevice)
            return (IBundledDevice) te;

        return null;
    }

}
