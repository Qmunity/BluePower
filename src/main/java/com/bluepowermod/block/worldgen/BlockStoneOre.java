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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class BlockStoneOre extends Block {

    private final boolean witherproof;

    public BlockStoneOre(boolean witherproof) {
        super(Properties.of().requiresCorrectToolForDrops().strength(5.0F, witherproof ? 2000.0F : 2F).sound(SoundType.STONE));
        BPBlocks.blockList.add(this);
        this.witherproof = witherproof;
    }

    public BlockStoneOre() {
        super(Properties.of().requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.STONE));
        BPBlocks.blockList.add(this);
        witherproof = false;
    }

    public BlockStoneOre(Properties properties) {
        super(properties);
        BPBlocks.blockList.add(this);
        witherproof = false;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (witherproof) {
            tooltip.add(Component.literal(MinecraftColor.RED.getChatColor() + "Witherproof"));
        }
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        if (witherproof)
            return !(entity instanceof WitherBoss) && !(entity instanceof WitherSkull) && super.canEntityDestroy(state, world, pos, entity);

        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (!witherproof)
            super.wasExploded(worldIn, pos, explosionIn);
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return !witherproof && super.dropFromExplosion(explosion);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

}
