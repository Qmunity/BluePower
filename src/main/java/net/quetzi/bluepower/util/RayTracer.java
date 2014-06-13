/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.references.Dependencies;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import cpw.mods.fml.common.Optional;

public class RayTracer {
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs) {
    
        return rayTrace(start, end, aabbs, new Vector3(0, 0, 0));
    }
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs, int x, int y, int z) {
    
        return rayTrace(start, end, aabbs, new Vector3(x, y, z));
    }
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs, Vector3 location) {
    
        return null;
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    public static final Cuboid6 getSelectedCuboid(MovingObjectPosition mop, EntityPlayer player, ForgeDirection face, Iterable<IndexedCuboid6> boxes,
            boolean unused) {
    
        List<Cuboid6> cuboids = new ArrayList<Cuboid6>();
        for (IndexedCuboid6 c : boxes)
            cuboids.add(c);
        
        return getSelectedCuboid(mop, player, face, cuboids);
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    public static final Cuboid6 getSelectedCuboid(MovingObjectPosition mop, EntityPlayer player, ForgeDirection face, Iterable<Cuboid6> boxes) {
    
        Vector3 hit = new Vector3(mop.hitVec.xCoord - mop.blockX, mop.hitVec.yCoord - mop.blockY, mop.hitVec.zCoord - mop.blockZ);
        
        for (Cuboid6 c : boxes) {
            boolean is = false;
            if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                boolean is2 = false;
                if (face == ForgeDirection.UP) {
                    if (c.max.y == hit.getY()) is2 = true;
                } else {
                    if (c.min.y == hit.getY()) is2 = true;
                }
                
                if (is2 && hit.getX() >= c.min.x && hit.getX() < c.max.x && hit.getZ() >= c.min.z && hit.getZ() < c.max.z) is = true;
            }
            
            if (face == ForgeDirection.NORTH || face == ForgeDirection.SOUTH) {
                boolean is2 = false;
                if (face == ForgeDirection.SOUTH) {
                    if (c.max.z == hit.getZ()) is2 = true;
                } else {
                    if (c.min.z == hit.getZ()) is2 = true;
                }
                
                if (is2 && hit.getX() >= c.min.x && hit.getX() < c.max.x && hit.getY() >= c.min.y && hit.getY() < c.max.y) is = true;
            }
            
            if (face == ForgeDirection.EAST || face == ForgeDirection.WEST) {
                boolean is2 = false;
                if (face == ForgeDirection.EAST) {
                    if (c.max.x == hit.getX()) is2 = true;
                } else {
                    if (c.min.x == hit.getX()) is2 = true;
                }
                
                if (is2 && hit.getY() >= c.min.y && hit.getY() < c.max.y && hit.getZ() >= c.min.z && hit.getZ() < c.max.z) is = true;
            }
            
            if (is) return c;
        }
        
        return null;
    }
    
    /*
     * The following methods are from CodeChickenLib, credits to ChickenBones for this. CodeChickenLib can be found here:
     * http://files.minecraftforge.net/CodeChickenLib/
     */
    public static Vec3 getCorrectedHeadVec(EntityPlayer player) {
    
        Vec3 v = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        if (player.worldObj.isRemote) {
            v.yCoord += player.getEyeHeight() - player.getDefaultEyeHeight();// compatibility with eye height changing mods
        } else {
            v.yCoord += player.getEyeHeight();
            if (player instanceof EntityPlayerMP && player.isSneaking()) v.yCoord -= 0.08;
        }
        return v;
    }
    
    public static Vec3 getStartVec(EntityPlayer player) {
    
        return getCorrectedHeadVec(player);
    }
    
    public static Vec3 getEndVec(EntityPlayer player) {
    
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = 4.5;
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }
    
}
