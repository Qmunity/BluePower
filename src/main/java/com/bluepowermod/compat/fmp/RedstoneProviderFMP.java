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

package com.bluepowermod.compat.fmp;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;

public class RedstoneProviderFMP implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstoneDevice) {
                if (p instanceof IFaceRedstoneDevice) {
                    if (((IFaceRedstoneDevice) p).getFace() == face)
                        return (IRedstoneDevice) p;
                } else {
                    if (face == ForgeDirection.UNKNOWN)
                        return (IRedstoneDevice) p;
                }
            }
        }

        if (face != null && face != ForgeDirection.UNKNOWN)
            for (TMultiPart p : tmp.jPartList())
                if (p instanceof IFaceRedstonePart && ((IFaceRedstonePart) p).getFace() == face.ordinal())
                    return new FMPRedstoneDevice(p);

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstoneDevice) {
                if (p instanceof IFaceBundledDevice) {
                    if (((IFaceBundledDevice) p).getFace() == face)
                        return (IBundledDevice) p;
                } else {
                    if (face == ForgeDirection.UNKNOWN)
                        return (IBundledDevice) p;
                }
            }
        }

        return null;
    }

}
