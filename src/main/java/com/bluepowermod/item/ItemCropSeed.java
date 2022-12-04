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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.RegistryObject;

public class ItemCropSeed extends Item implements IPlantable {

    public static RegistryObject<Block> block;

    public ItemCropSeed(RegistryObject<Block> blockCrop, Block blockSoil) {
        super(new Properties().tab(BPCreativeTabs.items));
        block = blockCrop;
    }

    @Override
    public InteractionResult useOn(UseOnContext itemUseContext) {
        Player player = itemUseContext.getPlayer();
        InteractionHand hand = itemUseContext.getHand();
        Direction facing = itemUseContext.getClickedFace();
        Level world = itemUseContext.getLevel();
        BlockPos pos = itemUseContext.getClickedPos();
        ItemStack itemStack = player.getItemInHand(hand);
        if (facing.ordinal() != 1) {
            return InteractionResult.PASS;
        } else if (player.mayUseItemAt(pos, facing, itemStack) && player.mayUseItemAt(pos.above(), facing, itemStack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos),  world, pos, Direction.UP, this) && world.isEmptyBlock(pos.above()) && world.getBlockState(pos).getBlock() instanceof FarmBlock) {
                world.setBlock(pos.above(), block.get().defaultBlockState(), 2);
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
        return block.get().defaultBlockState();
    }

}
