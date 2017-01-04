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

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

public class RedstoneProviderQmunityLib implements IRedstoneProvider {

    @Override
    public IRedstoneDevice getRedstoneDeviceAt(World world, BlockPos pos, EnumFacing side, EnumFacing face) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, pos);
        if (holder != null) {
            boolean foundOnlyFace = holder.getParts().size() > 0;
            for (IPart p : holder.getParts()) {
                if (p instanceof IRedstoneDeviceWrapper) {
                    if (p instanceof IFace) {
                        if (((IFace) p).getFace() == face)
                            return ((IRedstoneDeviceWrapper) p).getDeviceOnSide(side);
                    } else {
                        if (face == null)
                            return ((IRedstoneDeviceWrapper) p).getDeviceOnSide(side);
                    }
                } else if (p instanceof IRedstoneDevice) {
                    if (p instanceof IFace) {
                        if (((IFace) p).getFace() == face)
                            return (IRedstoneDevice) p;
                    } else {
                        if (face == null)
                            return (IRedstoneDevice) p;
                    }
                    foundOnlyFace = true;
                } else {
                    if (!(p instanceof IFace))
                        foundOnlyFace = false;
                }
            }
            if (foundOnlyFace)
                return RedstoneApi.getInstance().getReturnDevice();
        }

        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(World world, BlockPos pos, EnumFacing side, EnumFacing face) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, pos);
        if (holder != null) {
            boolean foundOnlyFace = holder.getParts().size() > 0;
            for (IPart p : holder.getParts()) {
                if (p instanceof IBundledDeviceWrapper) {
                    if (p instanceof IFace) {
                        if (((IFace) p).getFace() == face)
                            return ((IBundledDeviceWrapper) p).getBundledDeviceOnSide(side);
                    } else {
                        if (face == null)
                            return ((IBundledDeviceWrapper) p).getBundledDeviceOnSide(side);
                    }
                } else if (p instanceof IBundledDevice) {
                    if (p instanceof IFace) {
                        if (((IFace) p).getFace() == face)
                            return (IBundledDevice) p;
                    } else {
                        if (face == null)
                            return (IBundledDevice) p;
                    }
                    foundOnlyFace = true;
                } else {
                    if (!(p instanceof IFace))
                        foundOnlyFace = false;
                }
            }
            if (foundOnlyFace)
                return null;// RedstoneApi.getInstance().getReturnDevice();
        }

        return null;
    }

}
