package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.tile.tier1.gate.TileSynchronizer;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockGateSynchronizer extends BlockGateBase{
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileSynchronizer(blockPos, blockState);
    }

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
        if (onTick){
            gate.setPowered(Side.FRONT, false);
            if (gate instanceof TileSynchronizer synchronizer) {
                synchronizer.setLeftChipPowered(false);
                synchronizer.setRightChipPowered(false);
            }
            return true;
        }
        if (!(gate instanceof TileSynchronizer synchronizer)) return false;
        boolean oldBackInput = gate.isPowered(Side.BACK);
        boolean oldLeftInput = gate.isPowered(Side.LEFT);
        boolean oldRightInput = gate.isPowered(Side.RIGHT);
        Level level = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        boolean backInput = MultipartUtils.getRedstonePower(Side.BACK, state, level, pos) > 0;
        boolean leftInput = MultipartUtils.getRedstonePower(Side.LEFT, state, level, pos) > 0;
        boolean rightInput = MultipartUtils.getRedstonePower(Side.RIGHT, state, level, pos) > 0;
        if (oldBackInput != backInput){
            gate.setPowered(Side.BACK, backInput);
            if (backInput){
                synchronizer.setLeftChipPowered(false);
                synchronizer.setRightChipPowered(false);
            }
        }
        if (oldLeftInput != leftInput){
            gate.setPowered(Side.LEFT, leftInput);
            if (leftInput && !backInput && !synchronizer.isLeftChipPowered()){
                synchronizer.setLeftChipPowered(true);
            }
        }
        if (oldRightInput != rightInput){
            gate.setPowered(Side.RIGHT, rightInput);
            if (rightInput && !backInput && !synchronizer.isRightChipPowered()){
                synchronizer.setRightChipPowered(true);
            }
        }
        if (synchronizer.isLeftChipPowered() && synchronizer.isRightChipPowered()){
            gate.setPowered(Side.FRONT, true);
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            Direction dir = toDirection(Side.FRONT, state);
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
            return true;
        }
        return false;
    }
}
