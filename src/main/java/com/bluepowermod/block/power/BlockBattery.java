/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.tile.tier3.TileBattery;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class BlockBattery extends BlockContainerBase {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 6);

    public BlockBattery() {
        super(TileBattery.class);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));

    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileBattery::tickBattery;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
        if(tile != null && tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent()) {
            IPowerBase storage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null);
            double voltage = storage.getVoltage();
            int level = (int)((voltage / storage.getMaxVoltage()) * 6);
            return this.stateDefinition.any().setValue(LEVEL, level);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(LEVEL);
    }

}
