/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.raytrace;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.compat.fmp.IMultipartCompat;
import com.bluepowermod.references.Dependencies;

import cpw.mods.fml.common.Optional;

public class RayTracer {

    @Optional.Method(modid = Dependencies.FMP)
    public static final AxisAlignedBB getSelectedCuboid(MovingObjectPosition mop, EntityPlayer player, ForgeDirection face,
            Iterable<IndexedCuboid6> boxes, boolean unused) {

        List<Cuboid6> cuboids = new ArrayList<Cuboid6>();
        for (IndexedCuboid6 c : boxes)
            cuboids.add(c);

        return getSelectedCuboid(mop, player, face, cuboids);
    }

    @Optional.Method(modid = Dependencies.FMP)
    public static final AxisAlignedBB getSelectedCuboid(MovingObjectPosition mop, EntityPlayer player, ForgeDirection face, Iterable<Cuboid6> boxes) {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        for (Cuboid6 c : boxes)
            aabbs.add(c.toAABB());

        return getSelectedBox(mop, player, face, aabbs);
    }

    public static final AxisAlignedBB getSelectedBox(MovingObjectPosition mop, EntityPlayer player, ForgeDirection face, Iterable<AxisAlignedBB> boxes) {

        Vector3 hit = new Vector3(mop.hitVec.xCoord - mop.blockX, mop.hitVec.yCoord - mop.blockY, mop.hitVec.zCoord - mop.blockZ);

        for (AxisAlignedBB c : boxes) {
            boolean is = false;
            if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                boolean is2 = false;
                if (face == ForgeDirection.UP) {
                    if (c.maxY == hit.getY())
                        is2 = true;
                } else {
                    if (c.minY == hit.getY())
                        is2 = true;
                }

                if (is2 && hit.getX() >= c.minX && hit.getX() < c.maxX && hit.getZ() >= c.minZ && hit.getZ() < c.maxZ)
                    is = true;
            }

            if (face == ForgeDirection.NORTH || face == ForgeDirection.SOUTH) {
                boolean is2 = false;
                if (face == ForgeDirection.SOUTH) {
                    if (c.maxZ == hit.getZ())
                        is2 = true;
                } else {
                    if (c.minZ == hit.getZ())
                        is2 = true;
                }

                if (is2 && hit.getX() >= c.minX && hit.getX() < c.maxX && hit.getY() >= c.minY && hit.getY() < c.maxY)
                    is = true;
            }

            if (face == ForgeDirection.EAST || face == ForgeDirection.WEST) {
                boolean is2 = false;
                if (face == ForgeDirection.EAST) {
                    if (c.maxX == hit.getX())
                        is2 = true;
                } else {
                    if (c.minX == hit.getX())
                        is2 = true;
                }

                if (is2 && hit.getY() >= c.minY && hit.getY() < c.maxY && hit.getZ() >= c.minZ && hit.getZ() < c.maxZ)
                    is = true;
            }

            if (is)
                return c;
        }

        return null;
    }

    /**
     * @author amadornes
     * 
     * @return
     */
    public static BPPart getSelectedPart(MovingObjectPosition mop, EntityPlayer player, TileEntity tile) {

        return ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getClickedPart(new Vector3(mop.blockX, mop.blockY, mop.blockZ,
                player.worldObj), new Vector3(mop.hitVec, player.worldObj).subtract(mop.blockX, mop.blockY, mop.blockZ), player, tile);
    }

    /**
     * @author amadornes
     * 
     * @return
     */
    public static BPPart getSelectedPart(MovingObjectPosition mop, EntityPlayer player) {

        return getSelectedPart(mop, player, null);
    }

    public static boolean isSameBox(AxisAlignedBB a, AxisAlignedBB b) {

        if (a.minX == b.minX && a.minY == b.minY && a.minZ == b.minZ && a.maxX == b.maxX && a.maxY == b.maxY && a.maxZ == b.maxZ)
            return true;
        return false;
    }

    /*
     * The following methods are from CodeChickenLib, credits to ChickenBones for CodeChickenLib can be found here:
     * http://files.minecraftforge.net/CodeChickenLib/
     */
    public static Vec3 getCorrectedHeadVec(EntityPlayer player) {

        Vec3 v = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        if (player.worldObj.isRemote) {
            v.yCoord += player.getEyeHeight() - player.getDefaultEyeHeight();// compatibility with eye height changing mods
        } else {
            v.yCoord += player.getEyeHeight();
            if (player instanceof EntityPlayerMP && player.isSneaking())
                v.yCoord -= 0.08;
        }
        return v;
    }

    public static Vec3 getStartVec(EntityPlayer player) {

        return getCorrectedHeadVec(player);
    }

    public static Vec3 getEndVec(EntityPlayer player) {

        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = player.capabilities.isCreativeMode ? 5 : 4.5;
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }

    public static Vector3 getStartVector(EntityPlayer player) {

        return new Vector3(getCorrectedHeadVec(player));
    }

    public static Vector3 getEndVector(EntityPlayer player) {

        return new Vector3(getEndVec(player));
    }

    private static double minX;
    private static double minY;
    private static double minZ;
    private static double maxX;
    private static double maxY;
    private static double maxZ;

    public static BPMop rayTrace(double x, double y, double z, EntityPlayer player, AxisAlignedBB aabb) {

        return rayTrace(x, y, z, new Vector3(getStartVec(player)), new Vector3(getEndVec(player)), aabb);
    }

    public static BPMop rayTrace(double x, double y, double z, Vector3 start, Vector3 end, AxisAlignedBB aabb) {

        minX = aabb.minX;
        minY = aabb.minY;
        minZ = aabb.minZ;
        maxX = aabb.maxX;
        maxY = aabb.maxY;
        maxZ = aabb.maxZ;

        start = start.clone().add((-x), (-y), (-z));
        end = end.clone().add((-x), (-y), (-z));
        Vector3 vec1 = start.YZintercept(end, minX);
        Vector3 vec2 = start.YZintercept(end, maxX);
        Vector3 vec3 = start.XZintercept(end, minY);
        Vector3 vec4 = start.XZintercept(end, maxY);
        Vector3 vec5 = start.XYintercept(end, minZ);
        Vector3 vec6 = start.XYintercept(end, maxZ);

        if (!isVecInsideYZBounds(vec1))
            vec1 = null;
        if (!isVecInsideYZBounds(vec2))
            vec2 = null;
        if (!isVecInsideXZBounds(vec3))
            vec3 = null;
        if (!isVecInsideXZBounds(vec4))
            vec4 = null;
        if (!isVecInsideXYBounds(vec5))
            vec5 = null;
        if (!isVecInsideXYBounds(vec6))
            vec6 = null;

        Vector3 vec = null;

        if (vec1 != null && (vec == null || start.squareDistanceTo(vec1) < start.squareDistanceTo(vec)))
            vec = vec1;
        if (vec2 != null && (vec == null || start.squareDistanceTo(vec2) < start.squareDistanceTo(vec)))
            vec = vec2;
        if (vec3 != null && (vec == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec)))
            vec = vec3;
        if (vec4 != null && (vec == null || start.squareDistanceTo(vec4) < start.squareDistanceTo(vec)))
            vec = vec4;
        if (vec5 != null && (vec == null || start.squareDistanceTo(vec5) < start.squareDistanceTo(vec)))
            vec = vec5;
        if (vec6 != null && (vec == null || start.squareDistanceTo(vec6) < start.squareDistanceTo(vec)))
            vec = vec6;

        if (vec == null) {
            return null;
        } else {
            byte b0 = -1;

            if (vec == vec1)
                b0 = 4;
            if (vec == vec2)
                b0 = 5;
            if (vec == vec3)
                b0 = 0;
            if (vec == vec4)
                b0 = 1;
            if (vec == vec5)
                b0 = 2;
            if (vec == vec6)
                b0 = 3;

            aabb.minX += x;
            aabb.minY += y;
            aabb.minZ += z;
            aabb.maxX += x;
            aabb.maxY += y;
            aabb.maxZ += z;

            return new BPMop(new MovingObjectPosition((int) x, (int) y, (int) z, b0, vec.add(x, y, z).toVec3()), aabb);
        }
    }

    public static BPMop rayTrace(double x, double y, double z, EntityPlayer player, List<AxisAlignedBB> aabb) {

        return rayTrace(x, y, z, new Vector3(getStartVec(player)), new Vector3(getEndVec(player)), aabb);
    }

    public static BPMop rayTrace(double x, double y, double z, Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs) {

        BPMop mop = null;
        double dist = Double.POSITIVE_INFINITY;

        for (AxisAlignedBB aabb : aabbs) {
            BPMop m = rayTrace(x, y, z, start, end, aabb);
            if (m == null)
                continue;
            double d = m.hitVec.squareDistanceTo(start.toVec3());
            if (d < dist) {
                dist = d;
                mop = m;
            }
        }

        return mop;
    }

    private static boolean isVecInsideYZBounds(Vector3 vec) {

        return vec == null ? false : vec.getY() >= minY && vec.getY() <= maxY && vec.getZ() >= minZ && vec.getZ() <= maxZ;
    }

    private static boolean isVecInsideXZBounds(Vector3 vec) {

        return vec == null ? false : vec.getX() >= minX && vec.getX() <= maxX && vec.getZ() >= minZ && vec.getZ() <= maxZ;
    }

    private static boolean isVecInsideXYBounds(Vector3 vec) {

        return vec == null ? false : vec.getX() >= minX && vec.getX() <= maxX && vec.getY() >= minY && vec.getY() <= maxY;
    }

}
