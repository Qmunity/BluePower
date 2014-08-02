package com.bluepowermod.util;

import net.minecraft.util.AxisAlignedBB;

public class AABBUtils {

    public static AxisAlignedBB expand(AxisAlignedBB aabb, double amt) {

        aabb.minX -= amt;
        aabb.minY -= amt;
        aabb.minZ -= amt;
        aabb.maxX += amt;
        aabb.maxY += amt;
        aabb.maxZ += amt;

        return aabb;
    }

    public static AxisAlignedBB translate(AxisAlignedBB aabb, double x, double y, double z) {

        aabb.minX += x;
        aabb.minY += y;
        aabb.minZ += z;
        aabb.maxX += x;
        aabb.maxY += y;
        aabb.maxZ += z;

        return aabb;
    }

}
