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
import net.minecraftforge.common.util.ForgeDirection;

public abstract class LocationCache<CachedType> {
    private final CachedType[] cachedValue;

    public LocationCache(World world, int x, int y, int z, Object... extraArgs) {

        if (world == null)
            throw new NullPointerException("World can't be null!");
        cachedValue = (CachedType[]) new Object[6];
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            cachedValue[d.ordinal()] = getNewValue(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, extraArgs);
        }
    }

    protected abstract CachedType getNewValue(World world, int x, int y, int z, Object... extraArgs);

    public CachedType getValue(ForgeDirection side) {
        return cachedValue[side.ordinal()];
    }
}
