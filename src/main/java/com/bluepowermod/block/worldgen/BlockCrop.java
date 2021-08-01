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
import com.bluepowermod.reference.Refs;
import net.minecraft.block.*;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.block.RenderShape;
import net.minecraft.loot.LootContext;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.BlockGetter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
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
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

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
        if ((age == 6) && world.getBlockState(pos.below()).getBlock().isFertile(world.getBlockState(pos.below()), world, pos.below()) && world.getBlockState(pos.above()).getBlock().isAir(world.getBlockState(pos.above()), world, pos.above())) {
            world.setBlock(pos, getStateForAge(4), 2);
        }
        // If the bottom somehow becomes fully grown, correct it
        if ((age > 6) && (world.getBlockState(pos.below()).getBlock().isFertile(world.getBlockState(pos.below()), world, pos.below()))) {
            world.setBlock(pos, getStateForAge(4), 2);
        }
        if ((age == 7) && world.getBlockState(pos.below()).getBlock().isAir(world.getBlockState(pos.below()), world, pos.below())) {
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
    protected IItemProvider getBaseSeedId() {
        return BPItems.flax_seeds;
    }

    /**
     * boolean canFertilise
     */
    @Override
    public boolean isBonemealSuccess(Level world, Random rand, BlockPos pos, BlockState state) {
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
            age = age + MathHelper.nextInt(world.random, 2, 5);
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
        if (isMaxAge(state)) {
            for (int i = 0; i < 3; ++i) {
                if (RANDOM.nextInt(15) <= getAge(state)) {
                    drops.add(new ItemStack(Items.STRING, 1));
                }
            }
            if (RANDOM.nextBoolean()) {
                drops.add(new ItemStack(this.getBaseSeedId(), 1));
            }
        } else if (getAge(state) == 6) {
            drops.add(new ItemStack(this.getBaseSeedId(), 1 + RANDOM.nextInt(2)));
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
