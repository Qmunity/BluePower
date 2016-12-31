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
import net.minecraft.util.EnumFacing;;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;

public class RedstoneProviderFMP implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstoneDevice) {
                if (p instanceof IFace) {
                    if (((IFace) p).getFace() == face)
                        return (IRedstoneDevice) p;
                } else {
                    if (face == null)
                        return (IRedstoneDevice) p;
                }
            }
        }

        if (face != null && face != null)
            for (TMultiPart p : tmp.jPartList())
                if (p instanceof IFaceRedstonePart && ((IFaceRedstonePart) p).getFace() == face.ordinal())
                    return new FMPRedstoneDevice(p);

        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, int x, int y, int z, EnumFacing face, EnumFacing side) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IBundledDevice) {
                if (p instanceof IFace) {
                    if (((IFace) p).getFace() == face)
                        return (IBundledDevice) p;
                } else {
                    if (face == null)
                        return (IBundledDevice) p;
                }
            }
        }

        return null;
    }

}
