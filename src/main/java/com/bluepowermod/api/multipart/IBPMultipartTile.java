package com.bluepowermod.api.multipart;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IBPMultipartTile {
    void addState(BlockState state);
    BlockState getStateByFacing(Direction facing);
    void removeState(BlockState state);
    void changeState(BlockState oldState, BlockState newState);
    BlockEntity getTileForState(BlockState state);
    List<BlockState> getStates();
}
