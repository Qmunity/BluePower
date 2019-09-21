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
import net.minecraft.block.CropsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.List;
import java.util.Random;

public class BlockCrop extends CropsBlock implements IGrowable {
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public BlockCrop(Properties properties) {
        super(properties.hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement());
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
        BPBlocks.blockList.add(this);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.getBlock() instanceof BlockCrop) {
            if (isMaxAge(state) && world.getBlockState(pos.down()).getBlock() == this) {
                world.setBlockState(pos.down(), withAge(4), 2);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random) {

        int age = getAge(state);
        if (world.getLight(pos) >= 9) {
            if ((age < 6) && world.getBlockState(pos.down()).getBlock().isFertile(world.getBlockState(pos.down()), world, pos.down())) {
                if (random.nextInt(10) == 0) {
                    world.setBlockState(pos, withAge(age + 1), 2);
                    if (age == 5) {
                        world.setBlockState(pos.up(), withAge(7), 2);
                    }
                }
            }
        }
        if ((age == 6) && world.getBlockState(pos.down()).getBlock().isFertile(world.getBlockState(pos.down()), world, pos.down()) && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up())) {
            world.setBlockState(pos, withAge(4), 2);
        }
        // If the bottom somehow becomes fully grown, correct it
        if ((age > 6) && (world.getBlockState(pos.down()).getBlock().isFertile(world.getBlockState(pos.down()), world, pos.down()))) {
            world.setBlockState(pos, withAge(4), 2);
        }
        if ((age == 7) && world.getBlockState(pos.down()).getBlock().isAir(world.getBlockState(pos.down()), world, pos.down())) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return BPItems.flax_seeds;
    }

    /**
     * boolean canFertilise
     */
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    /**
     * fertilize
     * @param world
     * @param random
     */
    @Override
    public void grow(World world, Random random, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        if (world.isAirBlock(pos.up()) && (age < 7) && !(world.getBlockState(pos.down()).getBlock() instanceof BlockCrop)) {
            age = age + MathHelper.nextInt(world.rand, 2, 5);
            if (age >= 6) {
                world.setBlockState(pos, withAge(6), 2);
                world.setBlockState(pos.up(), withAge(7), 2);
            } else {
                world.setBlockState(pos, withAge(age), 2);
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
                drops.add(new ItemStack(this.getSeedsItem(), 1));
            }
        } else if (getAge(state) == 6) {
            drops.add(new ItemStack(this.getSeedsItem(), 1 + RANDOM.nextInt(2)));
        } else {
            drops.add(new ItemStack(this.getSeedsItem(), 1));
        }
        return drops;
    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return getAge(state) == 7;
    }

    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(this.getAgeProperty())];
    }



}
