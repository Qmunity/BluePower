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

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileBlockBreaker extends TileMachineBase {

    public TileBlockBreaker(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BLOCKBREAKER.get(), pos, state);
    }

    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        
        if (!level.isClientSide && newValue) {
            Direction direction = getFacingDirection();
            BlockState breakState = level.getBlockState(worldPosition.relative(direction));
            if (!canBreakBlock(breakState.getBlock(), level, breakState, worldPosition.relative(direction))) return;
            List<ItemStack> breakStacks = breakState.getBlock().getDrops(breakState, (ServerLevel) level, worldPosition.relative(direction),this);
            level.destroyBlock(worldPosition.relative(direction), false); // destroyBlock
            addItemsToOutputBuffer(breakStacks);
        }
    }

    private boolean canBreakBlock(Block block, Level world, BlockState state, BlockPos pos) {
    
        return !world.isEmptyBlock(pos) && !(block instanceof LiquidBlock) && state.getDestroySpeed(level, pos) > -1.0F;
    }
    
    @Override
    public boolean canConnectRedstone() {
        return true;
    }
}
