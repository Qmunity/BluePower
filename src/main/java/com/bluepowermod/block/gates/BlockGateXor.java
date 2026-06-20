package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BlockGateXor extends BlockGateAnd{
    public BlockGateXor(boolean inverted) {
        super(inverted);
    }

    @Override
    public Map<Side, Byte> getSidePower(BlockState state, TileGate gate){
        Map<Side, Byte> map = super.getSidePower(state, gate);
        boolean powered_back = map.get(Side.LEFT) == 0 && map.get(Side.RIGHT) == 0;
        map.put(Side.BACK, (byte) (powered_back ? 16 : 0));
        return map;
    }

    @Override
    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right) {
        boolean out = (left > 0 && right == 0) || (left == 0 && right > 0);
        return (byte) (out != inverted ? 16 : 0);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return super.canConnectRedstone(state, world, pos, side) && side != null && fromDirection(side.getOpposite(), state.getValue(ROTATION), DirectionHelper.ArrayFromDirection(state.getValue(FACING))) != Side.BACK;
    }
}
