package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlockGatePulseFormer extends BlockGateBase{
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
        boolean oldInput = gate.isPowered(Side.BACK);
        Level level = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        boolean newInput = MultipartUtils.getRedstonePower(Side.BACK, state, level, pos) > 0;
        if (!oldInput && newInput){
            gate.setPowered(Side.BACK, true);
            gate.setPowered(Side.FRONT, true);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            Direction dir = toDirection(Side.FRONT, state);
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
            return true;
        } else if (oldInput && !newInput){
            gate.setPowered(Side.BACK, false);
        }
        return false;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        TileGate gate = getGateTile(state, level.getBlockEntity(pos));
        if (gate.isPowered(Side.FRONT)){
            gate.setPowered(Side.FRONT, false);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            Direction dir = toDirection(Side.FRONT, state);
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
        }
    }
}
