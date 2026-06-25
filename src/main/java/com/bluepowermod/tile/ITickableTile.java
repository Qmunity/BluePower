package com.bluepowermod.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableTile {
    void tick(Level level, BlockPos pos, BlockState state);

    /**
     *  Only used for {@link com.bluepowermod.tile.TileBPMultipart}
     */
    default boolean canActuallyTick(){
        return true;
    }

    static void tick(Level level, BlockPos pos, BlockState state, BlockEntity be){
        if (be instanceof ITickableTile tickableTile){
            tickableTile.tick(level, pos, state);
        }
    }
}
