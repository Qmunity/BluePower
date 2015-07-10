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

package com.bluepowermod.helper;

import net.minecraft.world.World;
import uk.co.qmunity.lib.helper.BlockPos;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3i;

public class PartCache<CachedPart extends IPart> extends LocationCache<CachedPart> {

    public <T> PartCache(World world, int x, int y, int z, Class<? extends IPart> searchedParts) {

        this(world, new BlockPos(x, y, z), searchedParts);
    }

    public <T> PartCache(World world, BlockPos pos, Class<? extends IPart> searchedParts) {

        super(world, pos, searchedParts);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CachedPart getNewValue(World world, int x, int y, int z, Object... extraArgs) {

        return (CachedPart) MultipartCompatibility.getPart(world, x, y, z, (Class<? extends IPart>) extraArgs[0]);
    }

    @Override
    protected CachedPart getNewValue(World world, BlockPos pos, Object... extraArgs) {

        return (CachedPart) MultipartCompatibility.getPart(world, new Vec3i(pos), (Class<? extends IPart>) extraArgs[0]);
    }

}
