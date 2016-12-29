/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;


import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class AABBUtils {

    public static AxisAlignedBB expand(AxisAlignedBB aabb, double amt) {
        return aabb.expand(amt, amt, amt);
    }

    public static AxisAlignedBB translate(AxisAlignedBB aabb, double x, double y, double z) {
        return aabb.move(new Vec3d(x,y,z));
    }

}
