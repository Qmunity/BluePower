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

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;

public class RedstoneProviderQmunityLib implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null) {
            boolean found = false;
            for (IPart p : holder.getParts()) {
                if (p instanceof IRedstoneDevice) {
                    if (p instanceof IFaceRedstoneDevice) {
                        if (((IFaceRedstoneDevice) p).getFace() == face)
                            return (IRedstoneDevice) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN)
                            return (IRedstoneDevice) p;
                    }
                    found = true;
                }
            }
            if (found)
                return RedstoneApi.getInstance().getReturnDevice();
        }

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null) {
            for (IPart p : holder.getParts()) {
                if (p instanceof IBundledDevice) {
                    if (p instanceof IFaceBundledDevice) {
                        if (((IFaceBundledDevice) p).getFace() == face)
                            return (IBundledDevice) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN)
                            return (IBundledDevice) p;
                    }
                }
            }
        }

        return null;
    }

}
