package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGateRSLatch extends BlockGateLogic {
    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        byte leftIn = MultipartUtils.getRedstonePower(Side.LEFT, state, gate.getLevel(), gate.getBlockPos());
        byte rightIn = MultipartUtils.getRedstonePower(Side.RIGHT, state, gate.getLevel(), gate.getBlockPos());
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
