/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.vec;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.util.ForgeDirectionUtils;

public class Vector3Cube {

    private Vector3 min, max;

    public Vector3Cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

        this(minX, minY, minZ, maxX, maxY, maxZ, null);
    }

    public Vector3Cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, World world) {

        this(new Vector3(minX, minY, minZ, world), new Vector3(maxX, maxY, maxZ, world));
    }

    public Vector3Cube(Vector3 a, Vector3 b) {

        World w = a.getWorld();
        if (w == null)
            w = b.getWorld();

        double minX = Math.min(a.getX(), b.getX());
        double minY = Math.min(a.getY(), b.getY());
        double minZ = Math.min(a.getZ(), b.getZ());

        double maxX = Math.max(a.getX(), b.getX());
        double maxY = Math.max(a.getY(), b.getY());
        double maxZ = Math.max(a.getZ(), b.getZ());

        min = new Vector3(minX, minY, minZ, w);
        max = new Vector3(maxX, maxY, maxZ, w);
    }

    public Vector3Cube(AxisAlignedBB aabb) {

        this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public Vector3 getMin() {

        return min;
    }

    public Vector3 getMax() {

        return max;
    }

    public Vector3 getCenter() {

        return new Vector3((getMinX() + getMaxX()) / 2D, (getMinY() + getMaxY()) / 2D, (getMinZ() + getMaxZ()) / 2D, getMin().getWorld());
    }

    public double getMinX() {

        return min.getX();
    }

    public double getMinY() {

        return min.getY();
    }

    public double getMinZ() {

        return min.getZ();
    }

    public double getMaxX() {

        return max.getX();
    }

    public double getMaxY() {

        return max.getY();
    }

    public double getMaxZ() {

        return max.getZ();
    }

    public AxisAlignedBB toAABB() {

        return AxisAlignedBB.getBoundingBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    @Override
    public Vector3Cube clone() {

        return new Vector3Cube(min.clone(), max.clone());
    }

    public Vector3Cube expand(double size) {

        min.subtract(size, size, size);
        max.add(size, size, size);

        return this;
    }

    private ForgeDirection last = ForgeDirection.UNKNOWN;

    public Vector3Cube rotate90Degrees(ForgeDirection dir) {

        if (last == ForgeDirection.UNKNOWN) {
            last = dir;
        } else {
            dir = ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[ForgeDirectionUtils.getSide(last)][ForgeDirectionUtils.getSide(dir)]);
        }

        double mul = 1;
        ForgeDirection o = dir;
        switch (o) {
        case DOWN:
            mul = 0;
            break;
        case EAST:
            dir = ForgeDirection.SOUTH;
            break;
        case NORTH:
            dir = ForgeDirection.EAST;
            break;
        case SOUTH:
            dir = ForgeDirection.WEST;
            break;
        case UP:
            mul = 2;
            dir = ForgeDirection.NORTH;
            break;
        case WEST:
            dir = ForgeDirection.NORTH;
            break;
        default:
            break;

        }

        double rx = dir.offsetX * (Math.PI / 2) * mul;
        double ry = dir.offsetY * (Math.PI / 2) * mul;
        double rz = dir.offsetZ * (Math.PI / 2) * mul;

        Vector3 min = this.min.clone().subtract(0.5, 0.5, 0.5);
        min.rotate(rx, ry, rz);
        min.add(0.5, 0.5, 0.5);
        Vector3 max = this.max.clone().subtract(0.5, 0.5, 0.5);
        max.rotate(rx, ry, rz);
        max.add(0.5, 0.5, 0.5);

        Vector3Cube v3c = new Vector3Cube(min, max);

        this.min = v3c.min;
        this.max = v3c.max;

        return this;
    }

}
