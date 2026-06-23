package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGateMultiplexer extends BlockGateBase{
    @Override
    protected int getDelay(BlockState state, TileGate gate) {
        return 2;
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT;
    }

    @Override
    protected boolean checkPower(BlockState state, TileGate gate, boolean onTick) {
        boolean oldBackInput = gate.isPowered(Side.BACK);
        boolean oldLeftInput = gate.isPowered(Side.LEFT);
        boolean oldRightInput = gate.isPowered(Side.RIGHT);
        Level level = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        boolean backInput = MultipartUtils.getRedstonePower(Side.BACK, state, level, pos) > 0;
        boolean rightInput = MultipartUtils.getRedstonePower(Side.RIGHT, state, level, pos) > 0;
        boolean leftInput = MultipartUtils.getRedstonePower(Side.LEFT, state, level, pos) > 0;
        if (onTick) {
            if (!leftInput && !rightInput) {
                gate.setPowered(Side.FRONT, false);
            } else {
                gate.setPowered(Side.LEFT, leftInput);
                gate.setPowered(Side.RIGHT, rightInput);
                gate.setPowered(Side.FRONT, backInput ? leftInput : rightInput);
            }
            return true;
        } else {
            if (!leftInput && !rightInput){
                if (oldLeftInput) gate.setPowered(Side.LEFT, false);
                if (oldRightInput) gate.setPowered(Side.RIGHT, false);
                if (!oldBackInput && backInput){
                    gate.setPowered(Side.BACK, true);
                    gate.setPowered(Side.FRONT, true);
                    level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
                    Direction dir = toDirection(Side.FRONT, state);
                    BlockPos neighbor = pos.relative(dir);
                    BlockState neighborState = level.getBlockState(neighbor);
                    level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
                    return true;
                } else if (oldBackInput && !backInput){
                    gate.setPowered(Side.BACK, false);
                }
            } else {
                if (oldBackInput != backInput){
                    gate.setPowered(Side.BACK, backInput);
                }
            }
        }
        return oldLeftInput != leftInput || oldBackInput != backInput || oldRightInput != rightInput;
    }
}
