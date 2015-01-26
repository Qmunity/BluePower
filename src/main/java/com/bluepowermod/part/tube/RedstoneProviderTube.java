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

package com.bluepowermod.part.tube;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;

public class RedstoneProviderTube implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        PneumaticTube tube = MultipartCompatibility.getPart(world, x, y, z, PneumaticTube.class);
        if (tube != null)
            return RedstoneConductorTube.getDevice(tube);

        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return null;
    }

}
