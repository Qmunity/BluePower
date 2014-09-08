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

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.qmunity.lib.util.Dependencies;

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

}
