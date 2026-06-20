package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGateOr extends BlockGateAnd{
    public BlockGateOr(boolean inverted) {
        super(inverted);
    }

    @Override
    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right, TileGate gate) {
        boolean output = back > 0 || gate.isDisabled(Side.BACK) ||
                left > 0 || gate.isDisabled(Side.LEFT) ||
                right > 0 || gate.isDisabled(Side.RIGHT);
        return (byte) (output != inverted ? 16: 0);
    }
}

