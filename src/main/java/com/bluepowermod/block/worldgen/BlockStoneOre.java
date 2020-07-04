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
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.List;

public class BlockStoneOre extends Block {

    private final boolean witherproof;

    public BlockStoneOre(String name, boolean witherproof) {
        super(Properties.create(Material.ROCK).hardnessAndResistance(5.0F, witherproof ? 2000.0F : 2F).sound(SoundType.STONE));
        setRegistryName(Refs.MODID, name);
        BPBlocks.blockList.add(this);
        this.witherproof = witherproof;
    }

    public BlockStoneOre(String name) {
        super(Properties.create(Material.ROCK).hardnessAndResistance(5.0F).sound(SoundType.STONE));
        setRegistryName(Refs.MODID, name);
        BPBlocks.blockList.add(this);
        witherproof = false;
    }

    public BlockStoneOre(String name, Properties properties) {
        super(properties);
        setRegistryName(Refs.MODID, name);
        BPBlocks.blockList.add(this);
        witherproof = false;
    }

    // Allow storage blocks to be used as a beacon base
    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return this == BPBlocks.amethyst_block || this == BPBlocks.ruby_block || this == BPBlocks.malachite_block || this == BPBlocks.sapphire_block
                || this == BPBlocks.copper_block || this == BPBlocks.zinc_block || this == BPBlocks.silver_block
                || this == BPBlocks.tungsten_block;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (witherproof) {
            tooltip.add(new StringTextComponent(MinecraftColor.RED.getChatColor() + "Witherproof"));
        }
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        if (witherproof)
            return !(entity instanceof WitherEntity) && !(entity instanceof WitherSkullEntity) && super.canEntityDestroy(state, world, pos, entity);

        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!witherproof)
            super.onExplosionDestroy(worldIn, pos, explosionIn);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return !witherproof && super.canDropFromExplosion(explosion);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

}
