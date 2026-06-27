package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGateTransparentLatch extends BlockGateLogic{
    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT || side == Side.RIGHT;
    }

    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        byte front =(byte) (gate.isPowered(Side.FRONT) ? 16 : 0);
        byte leftIn = MultipartUtils.getRedstonePower(Side.LEFT,state, gate.getLevel(), gate.getBlockPos());
        byte backIn = MultipartUtils.getRedstonePower(Side.BACK,state, gate.getLevel(), gate.getBlockPos());
        if (backIn > 0) front = leftIn;
        return Map.of(Side.FRONT, front, Side.BACK, backIn, Side.LEFT, leftIn, Side.RIGHT, front);
    }
}
