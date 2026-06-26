package com.bluepowermod.tile.tier1.gate;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.bluepowermod.block.gates.BlockGateBase.toDirection;

public class TileRandomizer extends TileGate{
    public TileRandomizer(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 2 != 0 || !isPowered(Side.BACK)) return;
        List<Side> sidesToUpdate = new ArrayList<>();
        for (Side side : Side.values()){
            if (side == Side.BACK) continue;
            boolean newState = level.random.nextBoolean();
            if (isPowered(side) != newState){
                setPowered(side, newState);
                sidesToUpdate.add(side);
            }
        }
        if (!sidesToUpdate.isEmpty()){
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            for (Side side : sidesToUpdate){
                Direction dir = toDirection(side, state);
                BlockPos neighbor = pos.relative(dir);
                BlockState neighborState = level.getBlockState(neighbor);
                level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
            }
        }
    }
}
