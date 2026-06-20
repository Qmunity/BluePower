package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGateRSLatch extends BlockGateBase{
    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        Direction sideLeft = toDirection(Side.LEFT, state);
        Direction sideRight = toDirection(Side.RIGHT, state);
        byte leftIn = (byte) gate.getLevel().getSignal(gate.getBlockPos().relative(sideLeft), sideLeft);
        byte rightIn = (byte) gate.getLevel().getSignal(gate.getBlockPos().relative(sideRight), sideRight);
        boolean frontPowered = gate.isPowered(Side.FRONT);
        boolean backPowered = gate.isPowered(Side.BACK);
        if (!frontPowered && !backPowered ){
            if (rightIn == 0) backPowered = true;
            else if (leftIn == 0) frontPowered = true;
        }
        if (leftIn > 0 && rightIn > 0){
            frontPowered = false;
            backPowered = false;
        } else if (leftIn > 0 && frontPowered){
            frontPowered = false;
            backPowered = true;
        } else if (rightIn > 0 && backPowered){
            backPowered = false;
            frontPowered = true;
        }
        return Map.of(Side.FRONT, (byte) (frontPowered ? 16 : 0),
                Side.LEFT, (byte)(backPowered || leftIn > 0 ? 16 : 0),
                Side.RIGHT, (byte)(frontPowered || rightIn > 0 ? 16 : 0),
                Side.BACK, (byte)(backPowered ? 16 : 0));
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT || side == Side.BACK || (side == Side.LEFT && gate.isPowered(Side.BACK) || (side == Side.RIGHT && gate.isPowered(Side.FRONT)));
    }

    @Override
    protected void onBlockPlace(BlockState state, TileGate gate) {
        gate.setPowered(Side.FRONT, true);
        gate.setPowered(Side.RIGHT, true);
    }
}
