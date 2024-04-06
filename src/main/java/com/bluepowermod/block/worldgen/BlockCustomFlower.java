/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;

public class BlockCustomFlower extends BushBlock {
    public static final MapCodec<BlockCustomFlower> CODEC = simpleCodec(BlockCustomFlower::new);
    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
    public MapCodec<BlockCustomFlower> codec() {
        return CODEC;
    }

    public BlockCustomFlower(Properties properties) {
        super(properties.strength(0.0F).sound(SoundType.CROP).noCollission());
        BPBlocks.blockList.add(this);
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vec3d = state.getOffset(worldIn, pos);
        return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
    }

}
