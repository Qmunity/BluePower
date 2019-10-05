/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;


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
                return new AxisAlignedBB(aabb.minY, 1 - aabb.minZ, 1 - aabb.minX, aabb.maxY, 1 - aabb.maxZ, 1 - aabb.maxX);
            case WEST:
                return new AxisAlignedBB(1 - aabb.minY, 1 - aabb.minZ, aabb.minX, 1 - aabb.maxY, 1 - aabb.maxZ, aabb.maxX);
            case NORTH:
                return new AxisAlignedBB(1 - aabb.minX, 1 - aabb.minZ, 1 - aabb.minY, 1 - aabb.maxX, 1 - aabb.maxZ, 1 - aabb.maxY);
            case SOUTH:
                return new AxisAlignedBB(aabb.minX, 1 - aabb.minZ, aabb.minY, aabb.maxX, 1 - aabb.maxZ, aabb.maxY);
        }
        return aabb;
    }

    public static VoxelShape rotate (VoxelShape shape, Direction facing){
        VoxelShape out = VoxelShapes.empty();
        for(AxisAlignedBB aabb : shape.toBoundingBoxList()){
            aabb = rotate(aabb, facing);
            out = VoxelShapes.or(out, Block.makeCuboidShape(aabb.minX * 16, aabb.minY * 16, aabb.minZ * 16, aabb.maxX * 16, aabb.maxY * 16, aabb.maxZ * 16));
        }
        return out;
    }

}
