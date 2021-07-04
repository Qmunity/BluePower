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

import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.List;

public class TileBlockBreaker extends TileMachineBase {

    public TileBlockBreaker() {
        super(BPTileEntityType.BLOCKBREAKER);
    }

    @Override
    protected void redstoneChanged(boolean newValue) {
    
        super.redstoneChanged(newValue);
        
        if (!level.isClientSide && newValue) {
            Direction direction = getFacingDirection();
            BlockState breakState = level.getBlockState(worldPosition.relative(direction));
            if (!canBreakBlock(breakState.getBlock(), level, breakState, worldPosition.relative(direction))) return;
            List<ItemStack> breakStacks = breakState.getBlock().getDrops(breakState, (ServerWorld) level, worldPosition.relative(direction),this);
            level.destroyBlock(worldPosition.relative(direction), false); // destroyBlock
            addItemsToOutputBuffer(breakStacks);
        }
    }

    private boolean canBreakBlock(Block block, World world, BlockState state, BlockPos pos) {
    
        return !world.isEmptyBlock(pos) && !(block instanceof IFluidBlock) && state.getDestroySpeed(level, pos) > -1.0F;
    }
    
    @Override
    public boolean canConnectRedstone() {
        return true;
    }
}
