/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod;

import com.bluepowermod.api.BPApi.IBPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BluePowerAPI implements IBPApi {

    @Override
    public AlloyFurnaceRegistry getAlloyFurnaceRegistry() {

        return AlloyFurnaceRegistry.getInstance();
    }

    @Override
    public void loadSilkySettings(Level world, BlockPos pos, ItemStack stack) {
        BlockEntity te = world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        if (te == null)
            throw new IllegalStateException("This block doesn't have a tile entity?!");
        if (stack.isEmpty())
            throw new IllegalArgumentException("ItemStack is empty!");
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("tileData")) {
                if (te instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) te).readSilkyData(world, pos, tag.getCompound("tileData"));
                } else if (blockState.getBlock() instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) blockState.getBlock()).readSilkyData(world, pos, tag.getCompound("tileData"));
                } else {
                    CompoundTag tileTag = tag.getCompound("tileData");
                    tileTag.putInt("x", pos.getX());
                    tileTag.putInt("y", pos.getY());
                    tileTag.putInt("z", pos.getZ());
                    te.load(tileTag);
                }
            }
        }
    }
}
