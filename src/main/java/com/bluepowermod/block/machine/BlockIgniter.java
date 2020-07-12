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
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileIgniter;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BlockIgniter extends BlockContainerFacingBase {
    
    public BlockIgniter() {
    
        super(Material.ROCK, TileIgniter.class);
        setRegistryName(Refs.MODID, Refs.BLOCKIGNITER_NAME);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        TileIgniter tile = (TileIgniter) world.getTileEntity(pos);
        boolean orientation = state.getBlock() == this && state.get(FACING) == Direction.UP;
        return orientation && tile != null && tile.getIsRedstonePowered();
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return false;
    }
}
