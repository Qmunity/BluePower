/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.tile.tier3.TileBlulectricCable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.capabilities.BlockCapability;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricCable extends BlockBPCableBase implements EntityBlock {


    public BlockBlulectricCable() {
        super(2, 2);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBlulectricCable(pos, state);
    }

    @Override
    protected BlockCapability<?, Direction> getCapability() {
        return CapabilityBlutricity.BLUTRICITY_CAPABILITY;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileBlulectricCable::tickCable;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

}
