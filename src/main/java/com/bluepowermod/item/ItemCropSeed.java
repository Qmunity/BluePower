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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.bluepowermod.reference.Refs;

public class ItemCropSeed extends Item implements IPlantable {

    public static Block field_150925_a;

    public ItemCropSeed(Block blockCrop, Block blockSoil) {
        super(new Properties());
        field_150925_a = blockCrop;
        this.setRegistryName(Refs.MODID + ":" + Refs.FLAXSEED_NAME);
        BPItems.itemList.add(this);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext itemUseContext) {
        PlayerEntity player = itemUseContext.getPlayer();
        Hand hand = itemUseContext.getHand();
        Direction facing = itemUseContext.getFace();
        World world = itemUseContext.getWorld();
        BlockPos pos = itemUseContext.getPos();
        ItemStack itemStack = player.getHeldItem(hand);
        if (facing.ordinal() != 1) {
            return ActionResultType.PASS;
        } else if (player.canPlayerEdit(pos, facing, itemStack) && player.canPlayerEdit(pos.up(), facing, itemStack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos),  world, pos, Direction.UP, this) && world.isAirBlock(pos.up()) && world.getBlockState(pos).getBlock().isFertile(world.getBlockState(pos), world, pos)) {
                world.setBlockState(pos.up(), field_150925_a.getDefaultState(), 2);
                itemStack.setCount(itemStack.getCount() - 1);
                player.setHeldItem(hand, itemStack);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return field_150925_a.getDefaultState();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getTranslationKey()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }
}
