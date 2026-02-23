package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockGateToggleLatch extends BlockGateBase{
    @Override
    protected Map<Side, Byte> getSidePower(SignalGetter worldIn, BlockState state, BlockPos pos) {
        Direction sideLeft = toDirection(Side.LEFT, state);
        Direction sideRight = toDirection(Side.RIGHT, state);
        byte leftIn = (byte) worldIn.getSignal(pos.relative(sideLeft), sideLeft);
        byte rightIn = (byte) worldIn.getSignal(pos.relative(sideRight), sideRight);
        boolean frontPowered = state.getValue(POWERED_FRONT);
        boolean backPowered = state.getValue(POWERED_BACK);
        boolean leftPowered = state.getValue(POWERED_LEFT);
        boolean rightPowered = state.getValue(POWERED_RIGHT);
        if (leftPowered || rightPowered){ // Do nothing
            return Map.of(Side.FRONT, (byte) (frontPowered ? 16 : 0),
                    Side.LEFT, leftIn,
                    Side.RIGHT, rightIn,
                    Side.BACK, (byte)(backPowered ? 16 : 0));

        }
        if (leftIn > 0 || rightIn > 0){
            frontPowered = !frontPowered;
            backPowered = !backPowered;
        }
        return Map.of(Side.FRONT, (byte) (frontPowered ? 16 : 0),
                Side.LEFT, leftIn,
                Side.RIGHT, rightIn,
                Side.BACK, (byte)(backPowered ? 16 : 0));
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, BlockGetter blockAccess, BlockPos pos) {
        return side == Side.FRONT || side == Side.BACK;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(POWERED_FRONT, true);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        state = state.setValue(POWERED_FRONT, !state.getValue(POWERED_FRONT)).setValue(POWERED_BACK, !state.getValue(POWERED_BACK));
        level.setBlockAndUpdate(pos, state);
        for (Direction dir : DirectionHelper.ArrayFromDirection(state.getValue(FACING))){
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
        }
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
