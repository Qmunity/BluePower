package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockGateRSLatch extends BlockGateBase{
    @Override
    protected Map<Side, Byte> getSidePower(SignalGetter worldIn, BlockState state, BlockPos pos) {
        Direction sideLeft = toDirection(Side.LEFT, state);
        Direction sideRight = toDirection(Side.RIGHT, state);
        byte leftIn = (byte) worldIn.getSignal(pos.relative(sideLeft), sideLeft);
        byte rightIn = (byte) worldIn.getSignal(pos.relative(sideRight), sideRight);
        boolean frontPowered = state.getValue(POWERED_FRONT);
        boolean backPowered = state.getValue(POWERED_BACK);
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
    protected boolean isSideSource(Side side, BlockState blockState, BlockGetter blockAccess, BlockPos pos) {
        return side == Side.FRONT || side == Side.BACK || (side == Side.LEFT && blockState.getValue(POWERED_BACK) || (side == Side.RIGHT && blockState.getValue(POWERED_FRONT)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(POWERED_FRONT, true).setValue(POWERED_RIGHT, true);
    }
}
