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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * @author MineMaarten
 */

public class BlockCrackedBasalt extends BlockStoneOre {

    public BlockCrackedBasalt(String name) {
        super(name, Properties.of(Material.STONE).strength(25.0F).sound(SoundType.STONE));
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // When the random chance hit, spew lava.
        if (!world.isClientSide && (random.nextInt(100) == 0)) {
                spawnLava(world, pos, random);
        }
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getBlockTicks().scheduleTick(pos, this, 20);
    }

    private void spawnLava(Level world, BlockPos pos, Random random) {
        if (DateEventHandler.isEvent(Event.NEW_YEAR)) {
            DateEventHandler.spawnFirework(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        } else {
            FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    DateEventHandler.isEvent(Event.HALLOWEEN) ? Blocks.CARVED_PUMPKIN.defaultBlockState() : Blocks.LAVA.defaultBlockState());
            entity.setDeltaMovement( 1 + random.nextDouble(), (random.nextDouble() - 0.5) * 0.8D, (random.nextDouble() - 0.5) * 0.8D);
            entity.fallDistance = 1;// make this field that keeps track of the ticks set to 1, so it doesn't kill itself when it searches for a lava
            // block.
            entity.dropItem = false; // disable item drops when the falling block fails to place.
            world.addFreshEntity(entity);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        itemStacks.add(new ItemStack(Item.byBlock(BPBlocks.cracked_basalt_lava)));
        return itemStacks;
    }

    @Override
    public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
            for (int i = 0; i < 10; i++)
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextDouble(), pos.getY() + 1, pos.getZ() + rand.nextDouble(), (rand.nextDouble() - 0.5) * 0.2,
                        rand.nextDouble() * 0.1, (rand.nextDouble() - 0.5) * 0.2);
    }
}
