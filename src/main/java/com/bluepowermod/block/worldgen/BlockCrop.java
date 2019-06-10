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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockCrop extends CropsBlock implements IGrowable {

    public BlockCrop() {

        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
        this.setTranslationKey(Refs.FLAXCROP_NAME);
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
        BPBlocks.blockList.add(this);
    }

    @Override
    public String getTranslationKey() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getTranslationKey()));
    }

    String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        int l = getAge(source.getBlockState(pos));
        if (l <= 2) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        } else if (l <= 4) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else if (l <= 5) {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        } else {
           return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
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
    public void updateTick(World world, BlockPos pos, BlockState state, Random random) {

        int age = getAge(state);
        if (world.getLight(pos) >= 9) {
            if ((age < 6) && world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down())) {
                if (random.nextInt(10) == 0) {
                    world.setBlockState(pos, withAge(age + 1), 2);
                    if (age == 5) {
                        world.setBlockState(pos.up(), withAge(7), 2);
                    }
                }
            }
        }
        if ((age == 6) && world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down()) && world.getBlockState(pos.up()).getBlock().isAir(world.getBlockState(pos.up()), world, pos.up())) {
            world.setBlockState(pos, withAge(4), 2);
        }
        // If the bottom somehow becomes fully grown, correct it
        if ((age > 6) && (world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down()))) {
            world.setBlockState(pos, withAge(4), 2);
        }
        if ((age == 7) && world.getBlockState(pos.down()).getBlock().isAir(world.getBlockState(pos.down()), world, pos.down())) {
            world.setBlockToAir(pos);
        }
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * getSeed
     * @return
     */
    @Override
    protected Item getSeed() {
        return BPItems.flax_seeds;
    }

    /**
     * getCrop
     * @return
     */
    @Override
    protected Item getCrop() {
        return Items.STRING;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    /**
     * boolean canFertilise
     */
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
        return !isMaxAge(state);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(this.getSeed());
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
            age = age + MathHelper.getInt(world.rand, 2, 5);
            if (age >= 6) {
                world.setBlockState(pos, withAge(6), 2);
                world.setBlockState(pos.up(), withAge(7), 2);
            } else {
                world.setBlockState(pos, withAge(age), 2);
            }
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState state, int fortune) {

        super.getDrops(drops, world, pos, state, fortune);
        if (isMaxAge(state)) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (RANDOM.nextInt(15) <= getAge(state)) {
                    drops.add(new ItemStack(this.getCrop(), 1, 0));
                }
            }
            if (RANDOM.nextBoolean()) {
                drops.add(new ItemStack(this.getSeed(), 1, 0));
            }
        } else if (getAge(state) == 6) {
            drops.add(new ItemStack(this.getSeed(), 1 + RANDOM.nextInt(2), 0));
        } else {
            drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canSustainPlant(BlockState state, IBlockAccess world, BlockPos pos, Direction direction, IPlantable plantable) {
        return super.canPlaceBlockAt((World)world, pos) && world.isAirBlock(pos.up());
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        // This block is a fully grown base and the block above is air
        if ((getAge(world.getBlockState(pos)) == 6) && world.isAirBlock(pos.up())) {
            ((World)world).setBlockState(pos, withAge(4), 2);
        }
        // This block is a fully grown base and the block above is a fully grown top
        if ((getAge(world.getBlockState(pos)) == 6) && (world.getBlockState(pos.up()).getBlock() == this && getAge(world.getBlockState(pos.up())) == 7)) {
            if (!canBlockStay((World)world, pos, world.getBlockState(pos))) {
                ((World) world).setBlockToAir(pos);
                ((World) world).setBlockToAir(pos.up());
            }
        }
        checkAndDropBlock((World)world, pos, world.getBlockState(pos));
        checkAndDropBlock((World)world, pos.up(), world.getBlockState(pos.up()));
    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return getAge(state) == 7;
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, BlockState state) {
        if (!this.canBlockStay(world, pos, state)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World world, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos).getBlock() != this) return super.canBlockStay(world, pos, state);
        if (world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), Direction.UP, this)) {
            return true;
        }
        return (world.getBlockState(pos.down()).getBlock() == this) && (getAge(world.getBlockState(pos.down())) == 6);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }
}
