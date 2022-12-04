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

package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.machine.BlockIgniter;
import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.bluepowermod.block.machine.BlockIgniter.ACTIVE;
import static com.bluepowermod.block.machine.BlockIgniter.FACING;

public class TileIgniter extends TileBase implements IEjectAnimator {

    public TileIgniter(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.IGNITER.get(), pos, state);
    }

    @Override
    protected void redstoneChanged(boolean newValue) {
        super.redstoneChanged(newValue);
        BlockIgniter.setState(newValue, level, worldPosition);
        sendUpdatePacket();
        if (getIsRedstonePowered()) {
            ignite();
        } else {
            extinguish();
        }
    }

    private void ignite() {
        Direction facing = getBlockState().getValue(FACING);
        if (level.getBestNeighborSignal(worldPosition) > 0 && level.isEmptyBlock(worldPosition.relative(facing)) && level.getBlockState(worldPosition.relative(facing)).isAir()) {
            level.setBlockAndUpdate(worldPosition.relative(facing), Blocks.FIRE.defaultBlockState());
        }
    }

    private void extinguish() {
        Direction facing = getBlockState().getValue(FACING);
        Block target = level.getBlockState(worldPosition.relative(facing)).getBlock();
        if (level.getBestNeighborSignal(worldPosition) == 0 && (target == Blocks.FIRE || target == Blocks.NETHER_PORTAL)) {
            level.setBlockAndUpdate(worldPosition.relative(facing), Blocks.AIR.defaultBlockState());
        }
    }


    public static void tickIgniter(Level level, BlockPos pos, BlockState state, TileIgniter tileIgniter) {

        if (tileIgniter.getTicker() % 5 == 0) {
            tileIgniter.ignite();
        }
        tileIgniter.tickTileBase(level, pos, state, tileIgniter);
    }

    @Override
    public boolean isEjecting() {

        return getBlockState().getValue(ACTIVE);
    }
}
