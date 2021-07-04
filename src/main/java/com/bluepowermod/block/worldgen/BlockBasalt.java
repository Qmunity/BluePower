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

import com.bluepowermod.init.BPBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Quetzi on 03/09/14.
 */
public class BlockBasalt extends BlockStoneOre {
    public BlockBasalt(String name) {
        super(name);
    }

    @Deprecated
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ResourceLocation resourcelocation = this.getLootTable();
        if (resourcelocation == LootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootContext lootcontext = builder.withParameter(LootParameters.BLOCK_STATE, state).create(LootParameterSets.BLOCK);
            ServerWorld serverworld = lootcontext.getLevel();
            LootTable loottable = serverworld.getServer().getLootTables().get(resourcelocation);
            return loottable.getRandomItems(lootcontext);
        }
    }

}
