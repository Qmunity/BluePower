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
import com.bluepowermod.init.BPItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Random;

public class BlockCrop extends CropBlock implements BonemealableBlock {
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, -1.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public BlockCrop(Properties properties) {
        super(properties.strength(0.0F).sound(SoundType.CROP).randomTicks().noCollission());
        BPBlocks.blockList.add(this);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getBlock() instanceof BlockCrop) {
            if (isMaxAge(state) && world.getBlockState(pos.below()).getBlock() == this) {
                world.setBlock(pos.below(), getStateForAge(4), 2);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {

        int age = getAge(state);
        if (world.getLightEmission(pos) >= 9) {
            if ((age < 6) && world.getBlockState(pos.below()).getBlock().isFertile(world.getBlockState(pos.below()), world, pos.below())) {
                if (random.nextInt(10) == 0) {
                    world.setBlock(pos, getStateForAge(age + 1), 2);
                    if (age == 5) {
                        world.setBlock(pos.above(), getStateForAge(7), 2);
                    }
                }
            }
        }
        if ((age == 6) && world.getBlockState(pos.below()).getBlock().isFertile(world.getBlockState(pos.below()), world, pos.below()) && world.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
            world.setBlock(pos, getStateForAge(4), 2);
        }
        // If the bottom somehow becomes fully grown, correct it
        if ((age > 6) && (world.getBlockState(pos.below()).getBlock().isFertile(world.getBlockState(pos.below()), world, pos.below()))) {
            world.setBlock(pos, getStateForAge(4), 2);
        }
        if ((age == 7) && world.getBlockState(pos.below()).getBlock() == Blocks.AIR) {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return BPItems.flax_seeds.get();
    }

    /**
     * boolean canFertilise
     */
    @Override
    public boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    /**
     * fertilize
     * @param world
     */
    @Override
    public void growCrops(Level world, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        if (world.isEmptyBlock(pos.above()) && (age < 7) && !(world.getBlockState(pos.below()).getBlock() instanceof BlockCrop)) {
            age = age + UniformInt.of(2, 5).sample(world.random);
            if (age >= 6) {
                world.setBlock(pos, getStateForAge(6), 2);
                world.setBlock(pos.above(), getStateForAge(7), 2);
            } else {
                world.setBlock(pos, getStateForAge(age), 2);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {

        List<ItemStack> drops = super.getDrops(state, builder);
        Random random = new Random();
        if (isMaxAge(state)) {
            for (int i = 0; i < 3; ++i) {
                if (random.nextInt(15) <= getAge(state)) {
                    drops.add(new ItemStack(Items.STRING, 1));
                }
            }
            if (random.nextBoolean()) {
                drops.add(new ItemStack(this.getBaseSeedId(), 1));
            }
        } else if (getAge(state) == 6) {
            drops.add(new ItemStack(this.getBaseSeedId(), 1 + random.nextInt(2)));
        } else {
            drops.add(new ItemStack(this.getBaseSeedId(), 1));
        }
        return drops;
    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return getAge(state) == 7;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(this.getAgeProperty())];
    }



}
