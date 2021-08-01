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
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBattery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.BlockEntity;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class BlockBattery extends BlockContainerBase {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 6);

    public BlockBattery() {
        super(Material.METAL, TileBattery.class);
        setRegistryName(Refs.MODID, Refs.BATTERYBLOCK_NAME);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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
