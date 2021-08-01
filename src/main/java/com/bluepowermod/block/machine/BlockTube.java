/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.machine;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier2.TileTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.Shapes;
import net.minecraft.world.BlockGetter;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * @author MoreThanHidden
 */
public class BlockTube extends PipeBlock implements IBPPartBlock {
    public BlockTube() {
        super(0.25F, Properties.of(Material.PISTON).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.makeConnections(context.getLevel(), context.getClickedPos());
    }

    public BlockState makeConnections(BlockGetter world, BlockPos pos) {
        Block blockDown = world.getBlockState(pos.below()).getBlock();
        Block blockUp = world.getBlockState(pos.above()).getBlock();
        Block blockNorth = world.getBlockState(pos.north()).getBlock();
        Block blockEast = world.getBlockState(pos.east()).getBlock();
        Block blockSouth = world.getBlockState(pos.south()).getBlock();
        Block blockWest = world.getBlockState(pos.west()).getBlock();
        return this.defaultBlockState()
                .setValue(DOWN, blockDown == this)
                .setValue(UP, blockUp == this )
                .setValue(NORTH, blockNorth == this)
                .setValue(EAST, blockEast == this)
                .setValue(SOUTH, blockSouth == this )
                .setValue(WEST, blockWest == this);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.box(4, 4, 4, 12, 12, 12);
    }

    @Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockState state, BlockGetter world) {
        return new TileTube();
    }
}
