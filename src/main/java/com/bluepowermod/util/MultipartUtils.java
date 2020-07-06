/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.util;

import com.bluepowermod.tile.TileBPMultipart;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class MultipartUtils {

    /**
     * Returns the look vector and max reach distance vector relative to the multipart block position.
     * based on MC Multipart
     * @param entity
     */
    public static Pair<Vector3d, Vector3d> getRayTraceVectors(Entity entity) {
        float pitch = entity.rotationPitch;
        float yaw = entity.rotationYaw;
        Vector3d start = new Vector3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());
        float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = -MathHelper.cos(-pitch * 0.017453292F);
        float f4 = MathHelper.sin(-pitch * 0.017453292F);
        float f5 = f2 * f3;
        float f6 = f1 * f3;
        double d3 = 5.0D;
        if (entity instanceof ServerPlayerEntity) {
            d3 = ((ServerPlayerEntity) entity).getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        }
        Vector3d end = start.add(f5 * d3, f4 * d3, f6 * d3);
        return Pair.of(start, end);
    }

    /**
     * Returns the closest Multipart state to the player in a given position.
     * @param entity
     * @param pos
     */
    @Nullable
    public static BlockState getClosestState(Entity entity, BlockPos pos){
        TileEntity te = entity.world.getTileEntity(pos);
        BlockState state = null;
        double distance = Double.POSITIVE_INFINITY;
        if(te instanceof TileBPMultipart) {
            Pair<Vector3d, Vector3d> lookVec = MultipartUtils.getRayTraceVectors(entity);
            for (BlockState part : ((TileBPMultipart) te).getStates()) {
                RayTraceResult res = part.getRaytraceShape(entity.world, pos, ISelectionContext.dummy()).rayTrace(lookVec.getLeft(), lookVec.getRight(), pos);
                if (res != null) {
                    double partDistance = lookVec.getLeft().squareDistanceTo(res.getHitVec());
                    if(distance > partDistance) {
                        distance = partDistance;
                        state = part;
                    }
                }
            }
        }
        return state;
    }
}
