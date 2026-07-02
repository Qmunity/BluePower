package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class BlockGateToggleLatch extends BlockGateLogic {
    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        byte leftIn = MultipartUtils.getRedstonePower(Side.LEFT, state, gate.getLevel(), gate.getBlockPos());
        byte rightIn = MultipartUtils.getRedstonePower(Side.RIGHT, state, gate.getLevel(), gate.getBlockPos());
        boolean frontPowered = gate.isPowered(Side.FRONT);
        boolean backPowered = gate.isPowered(Side.BACK);
        boolean leftPowered = gate.isPowered(Side.LEFT);
        boolean rightPowered = gate.isPowered(Side.RIGHT);
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
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT || side == Side.BACK;
    }


    @Override
    protected void onBlockPlace(BlockState state, TileGate gate) {
        gate.setPowered(Side.FRONT, true);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        TileGate gate = getGateTile(state, level.getBlockEntity(pos));
        gate.setPowered(Side.FRONT, !gate.isPowered(Side.FRONT));
        gate.setPowered(Side.BACK, !gate.isPowered(Side.BACK));
        for (Direction dir : DirectionHelper.ArrayFromDirection(state.getValue(FACING))){
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
        }
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
