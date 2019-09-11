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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author MoreThanHidden
 */

public class BlockBattery extends BlockContainerBase {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 6);

    public BlockBattery() {
        super(Material.IRON, TileBattery.class);
        setRegistryName(Refs.MODID, Refs.BATTERYBLOCK_NAME);
        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, 0));

    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld worldIn, BlockPos pos, BlockPos pos2, Hand hand) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile != null && tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent()) {
            IPowerBase storage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null);
            double voltage = storage.getVoltage();
            int level = (int)((voltage / storage.getMaxVoltage()) * 6);
            return this.stateContainer.getBaseState().with(LEVEL, level);
        }
        return super.getStateForPlacement(state, facing, state2, worldIn, pos, pos2, hand);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(LEVEL);
    }

}
