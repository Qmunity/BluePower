package com.bluepowermod.block.gates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGateOr extends BlockGateAnd{
    public BlockGateOr(boolean inverted) {
        super(inverted);
    }

    @Override
    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right) {
        boolean output = back > 0 || left > 0 || right > 0;
        return (byte) (output != inverted ? 16: 0);
    }
}

