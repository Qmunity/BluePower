/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.util;

import com.bluepowermod.tile.TileBPMultipart;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
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
    public static Pair<Vec3, Vec3> getRayTraceVectors(Entity entity) {
        float pitch = entity.xRotO;
        float yaw = entity.yRotO;
        Vec3 start = new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        float f1 = (float)Math.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = (float)Math.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = -(float)Math.cos(-pitch * 0.017453292F);
        float f4 = (float)Math.sin(-pitch * 0.017453292F);
        float f5 = f2 * f3;
        float f6 = f1 * f3;
        double d3 = 5.0D;
        if (entity instanceof ServerPlayer) {
            d3 = ((ServerPlayer) entity).getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        }
        Vec3 end = start.add(f5 * d3, f4 * d3, f6 * d3);
        return Pair.of(start, end);
    }

    /**
     * Returns the closest Multipart state to the player in a given position.
     * @param entity
     * @param pos
     */
    @Nullable
    public static BlockState getClosestState(Entity entity, BlockPos pos){
        Pair<Vec3, Vec3> lookVec = MultipartUtils.getRayTraceVectors(entity);
        return getClosestState(entity.level, lookVec.getLeft(), lookVec.getRight(), pos);
    }

    /**
     * Returns the closest Multipart state to the Vec3 in in a given position.
     * @param world
     * @param start
     * @param end
     * @param pos
     */
    @Nullable
    public static BlockState getClosestState(Level world, Vec3 start, Vec3 end, BlockPos pos){
        BlockEntity te = world.getBlockEntity(pos);
        BlockState state = null;
        double distance = Double.POSITIVE_INFINITY;
        if(te instanceof TileBPMultipart) {
            for (BlockState part : ((TileBPMultipart) te).getStates()) {
                BlockHitResult res = part.getVisualShape(world, pos, CollisionContext.empty()).clip(start, end, pos);
                if (res != null) {
                    double partDistance = start.distanceToSqr(res.getLocation());
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
