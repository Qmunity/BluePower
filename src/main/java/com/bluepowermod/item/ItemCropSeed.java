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

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.InteractionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockGetter;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.bluepowermod.reference.Refs;

import net.minecraft.world.item.Item.Properties;

import InteractionResult;

public class ItemCropSeed extends Item implements IPlantable {

    public static Block field_150925_a;

    public ItemCropSeed(Block blockCrop, Block blockSoil) {
        super(new Properties().tab(BPCreativeTabs.items));
        field_150925_a = blockCrop;
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXSEED_NAME);
        BPItems.itemList.add(this);
    }

    @Override
    public InteractionResult useOn(ItemUseContext itemUseContext) {
        Player player = itemUseContext.getPlayer();
        InteractionHand hand = itemUseContext.getHand();
        Direction facing = itemUseContext.getClickedFace();
        World world = itemUseContext.getLevel();
        BlockPos pos = itemUseContext.getClickedPos();
        ItemStack itemStack = player.getItemInHand(hand);
        if (facing.ordinal() != 1) {
            return InteractionResult.PASS;
        } else if (player.mayUseItemAt(pos, facing, itemStack) && player.mayUseItemAt(pos.above(), facing, itemStack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos),  world, pos, Direction.UP, this) && world.isEmptyBlock(pos.above()) && world.getBlockState(pos).getBlock() instanceof FarmlandBlock) {
                world.setBlock(pos.above(), field_150925_a.defaultBlockState(), 2);
                itemStack.setCount(itemStack.getCount() - 1);
                player.setItemInHand(hand, itemStack);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return field_150925_a.defaultBlockState();
    }

}
