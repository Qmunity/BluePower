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
import com.bluepowermod.util.DateEventHandler;
import com.bluepowermod.util.DateEventHandler.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * 
 * @author MineMaarten
 */

public class BlockCrackedBasalt extends BlockStoneOre {

    public BlockCrackedBasalt(String name) {

        super(name);
        setTickRandomly(true);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer playerIn) {
        if (world.getBlockState(pos) == this.getDefaultState()) {
            world.setBlockState(pos, this.getStateFromMeta(1), 2);
            world.scheduleBlockUpdate(pos, this, 1, 1);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        int meta =  this.getMetaFromState(world.getBlockState(pos));
        // When this block was active already (meta > 0) or when the random chance hit, spew lava.
        if (!world.isRemote && (meta > 0 || random.nextInt(100) == 0)) {

            if (meta > 4) {
                spawnLava(world, pos, random);
            }
            if (meta < 15) {
                if (random.nextInt(40) == 0 || meta == 0)
                    world.setBlockState(pos, this.getStateFromMeta(meta + 1), 2);
                world.scheduleBlockUpdate(pos, this, 1, 1);
            } else {
                world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
            }
        }
    }

    @Override
    protected boolean canSilkHarvest() {

        return false;
    }

    private void spawnLava(World world, BlockPos pos, Random random) {
        if (DateEventHandler.isEvent(Event.NEW_YEAR)) {
            DateEventHandler.spawnFirework(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        } else {
            EntityFallingBlock entity = new EntityFallingBlock(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    DateEventHandler.isEvent(Event.HALLOWEEN) ? Blocks.LIT_PUMPKIN.getDefaultState() : Blocks.FLOWING_LAVA.getDefaultState());
            entity.motionY = 1 + random.nextDouble();
            entity.motionX = (random.nextDouble() - 0.5) * 0.8D;
            entity.motionZ = (random.nextDouble() - 0.5) * 0.8D;
            entity.fallTime = 1;// make this field that keeps track of the ticks set to 1, so it doesn't kill itself when it searches for a lava
            // block.
            entity.shouldDropItem = false; // disable item drops when the falling block fails to place.
            world.spawnEntity(entity);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BPBlocks.basalt_cobble);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        if (getMetaFromState(world.getBlockState(pos)) > 0) {
            for (int i = 0; i < 10; i++)
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + rand.nextDouble(), pos.getY() + 1, pos.getZ() + rand.nextDouble(), (rand.nextDouble() - 0.5) * 0.2,
                        rand.nextDouble() * 0.1, (rand.nextDouble() - 0.5) * 0.2);
        }
    }
}
