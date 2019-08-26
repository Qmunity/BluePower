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

import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemSickle extends ToolItem {

    public    Item    customCraftingMaterial = Items.AIR;
    protected boolean canRepair              = true;

    private static final Set toolBlocks = Sets.newHashSet(ItemTags.LEAVES, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS,
            Blocks.NETHER_WART, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.SUGAR_CANE, Blocks.TALL_GRASS, Blocks.VINE, Blocks.LILY_PAD,
            BlockTags.SMALL_FLOWERS);

    public ItemSickle(IItemTier itemTier, String name, Item repairItem) {
        super(itemTier.getHarvestLevel(),1.4F, itemTier, toolBlocks, new Properties());
        this.setRegistryName(Refs.MODID + ":" + name);
        this.customCraftingMaterial = repairItem;
        BPItems.itemList.add(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((state.getMaterial() == Material.LEAVES) || (state.getMaterial() == Material.PLANTS) || toolBlocks.contains(state)) {
            return this.efficiency;
        }
        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity par2EntityLivingBase, LivingEntity par3EntityLivingBase) {

        itemStack.setDamage(itemStack.getDamage() -2);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

        boolean used = false;

        if (!(entityLiving instanceof PlayerEntity)) return false;
        PlayerEntity player = (PlayerEntity) entityLiving;

        if (state.getBlock().isFoliage(state, world, pos)) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        Block blockToCheck = world.getBlockState(pos.add(i,j,k)).getBlock();
                        BlockState meta = world.getBlockState(pos.add(i,j,k));
                        if (blockToCheck.isFoliage(meta, world, pos.add(i,j,k))) {
                            if (blockToCheck.canHarvestBlock(world.getBlockState(pos.add(i,j,k)), world, pos.add(i,j,k), player)) {
                                blockToCheck.harvestBlock(world, player, pos.add(i,j,k), meta, null, stack);
                            }
                            world.setBlockState(pos.add(i,j,k), Blocks.AIR.getDefaultState());
                            used = true;
                        }
                    }
                }
            }
            if (used) {
                stack.setDamage(stack.getDamage() -1);
            }
            return used;
        }

        if ((state != null) && (state.getBlock() instanceof LilyPadBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.add(i,0,j)).getBlock();
                    BlockState meta = world.getBlockState(pos.add(i,0,j));
                    if (blockToCheck != null && blockToCheck instanceof LilyPadBlock) {
                        if (blockToCheck.canHarvestBlock(meta, world, pos.add(i,0,j), player)) {
                            blockToCheck.harvestBlock(world, player, pos.add(i,0,j), meta, null, stack);
                        }
                        world.setBlockState(pos.add(i,0,j), Blocks.AIR.getDefaultState());
                        used = true;
                    }
                }
            }
        }
        if ((state != null) && !(state.getBlock() instanceof LilyPadBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.add(i,0,j)).getBlock();
                    BlockState meta = world.getBlockState(pos.add(i,0,j));
                    if (blockToCheck != null) {
                        if (blockToCheck instanceof BushBlock && !(blockToCheck instanceof LilyPadBlock)) {
                            if (blockToCheck.canHarvestBlock(world.getBlockState(pos.add(i,0,j)), world,  pos.add(i,0,j), player)) {
                                blockToCheck.harvestBlock(world, player, pos.add(i,0,j), meta, null, stack);
                            }
                            world.setBlockState(pos.add(i,0,j), Blocks.AIR.getDefaultState());
                            used = true;
                        }
                    }
                }
            }
        }
        if (used) {
            stack.setDamage(stack.getDamage() -1);
        }
        return used;
    }

    @Override
    public boolean getIsRepairable(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
