/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;


import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class AABBUtils {

    public static AxisAlignedBB expand(AxisAlignedBB aabb, double amt) {
        return aabb.expand(amt, amt, amt);
    }

    public static AxisAlignedBB translate(AxisAlignedBB aabb, double x, double y, double z) {
        return aabb.offset(x,y,z);
    }

    public static AxisAlignedBB rotate (AxisAlignedBB aabb, Direction facing){
        switch (facing){
            case UP:
                return aabb;
            case DOWN:
                return new AxisAlignedBB(aabb.minX, 1 - aabb.minY, aabb.minZ, aabb.maxX, 1 - aabb.maxY, aabb.maxZ);
            case EAST:
                return new AxisAlignedBB(aabb.minY, aabb.minX, aabb.minZ, aabb.maxY, aabb.maxX, aabb.maxZ);
            case WEST:
                return new AxisAlignedBB(1 - aabb.minY, aabb.minX, aabb.minZ, 1 - aabb.maxY, aabb.maxX, aabb.maxZ);
            case NORTH:
                return new AxisAlignedBB(aabb.minX, aabb.minZ, 1 - aabb.minY, aabb.maxX, aabb.maxZ, 1 - aabb.maxY);
            case SOUTH:
                return new AxisAlignedBB(aabb.minX, aabb.minZ, aabb.minY, aabb.maxX, aabb.maxZ, aabb.maxY);
        }
        return aabb;
    }

}
