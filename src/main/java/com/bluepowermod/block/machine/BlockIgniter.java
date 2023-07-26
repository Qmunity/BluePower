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

package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerFacingBase;
import com.bluepowermod.tile.tier1.TileIgniter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class BlockIgniter extends BlockContainerFacingBase {
    
    public BlockIgniter() {
        super(TileIgniter.class);
    }

    @Override
    public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
        TileIgniter tile = (TileIgniter) world.getBlockEntity(pos);
        boolean orientation = state.getBlock() == this && state.getValue(FACING) == Direction.UP;
        return orientation && tile != null && tile.getIsRedstonePowered();
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return false;
    }
}
