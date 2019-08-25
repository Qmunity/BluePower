/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;
import java.util.Random;

/**
 * Created by Quetzi on 03/09/14.
 */
public class BlockTeslatiteOre extends BlockItemOre {

    public BlockTeslatiteOre(String type) {
        super(type);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(rand, 2, 5);
    }

    //TODO Loot Table
    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        List<ItemStack> stacks = NonNullList.create();
        stacks.add(new ItemStack(BPItems.teslatite_dust));
        return stacks;
    }

}
