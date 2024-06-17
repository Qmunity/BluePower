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

package com.bluepowermod.item;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

import java.util.Set;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class ItemSickle extends DiggerItem {

    private Item customCraftingMaterial;

    private static final Set toolBlocks = Sets.newHashSet(ItemTags.LEAVES, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS,
            Blocks.NETHER_WART, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.SUGAR_CANE, Blocks.TALL_GRASS, Blocks.VINE, Blocks.LILY_PAD,
            BlockTags.SMALL_FLOWERS);

    public ItemSickle(Tier itemTier, Item repairItem) {
        super(itemTier, BlockTags.MINEABLE_WITH_HOE, new Properties().attributes(createAttributes(itemTier, 2,-1.4F)));
        this.customCraftingMaterial = repairItem;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((state.getBlock() instanceof LeavesBlock) || (state.getBlock() instanceof GrassBlock) || toolBlocks.contains(state)) {
            return super.getDestroySpeed(stack, state);
        }
        return 1.0F;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

        boolean used = false;

        if (!(entityLiving instanceof Player player)) return false;

        if ( state.is(BlockTags.LEAVES) || state.getBlock() instanceof LeavesBlock) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        BlockState blockToCheck = world.getBlockState(pos.offset(i,j,k));
                        if (blockToCheck.is(BlockTags.LEAVES) || blockToCheck.getBlock() instanceof LeavesBlock) {
                            if (blockToCheck.canHarvestBlock(world, pos.offset(i,j,k), player)) {
                                world.destroyBlock(pos.offset(i,j,k), true);
                            }
                            used = true;
                        }
                    }
                }
            }
            if (used) {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return used;
        }

        if ((state.getBlock() instanceof WaterlilyBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.offset(i,0,j)).getBlock();
                    BlockState meta = world.getBlockState(pos.offset(i,0,j));
                    if (blockToCheck instanceof WaterlilyBlock) {
                        if (blockToCheck.canHarvestBlock(meta, world, pos.offset(i,0,j), player)) {
                            world.destroyBlock(pos.offset(i,0,j), true);
                        }
                        used = true;
                    }
                }
            }
        }

        if (!(state.getBlock() instanceof WaterlilyBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.offset(i,0,j)).getBlock();
                    if (blockToCheck instanceof BushBlock && !(blockToCheck instanceof WaterlilyBlock)) {
                        if (blockToCheck.canHarvestBlock(world.getBlockState(pos.offset(i,0,j)), world,  pos.offset(i,0,j), player)) {
                            world.destroyBlock(pos.offset(i,0,j), true);
                        }
                        used = true;
                    }
                }
            }
        }

        if (used) {
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
        return used;
    }

    @Override
    public boolean isValidRepairItem(ItemStack is1, ItemStack is2) {
        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
