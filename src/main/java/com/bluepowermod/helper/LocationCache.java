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

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class LocationCache<CachedType> {
    private final CachedType[] cachedValue;

    public LocationCache(Level world, BlockPos pos, Object... extraArgs) {

        if (world == null)
            throw new NullPointerException("World can't be null!");
        cachedValue = (CachedType[]) new Object[6];
        for (Direction d : Direction.values()) {
            cachedValue[d.ordinal()] = getNewValue(world, pos.relative(d), extraArgs);
        }
    }

    protected abstract CachedType getNewValue(Level world, BlockPos pos, Object... extraArgs);

    public CachedType getValue(Direction side) {
        return cachedValue[side.ordinal()];
    }
}
