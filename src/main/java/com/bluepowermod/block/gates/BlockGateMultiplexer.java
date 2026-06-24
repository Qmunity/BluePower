package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateMultiplexer extends BlockGateLogic{
    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT;
    }

    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        Map<Side, Byte> map = new HashMap<>();
        Level worldIn = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        byte left = MultipartUtils.getRedstonePower(Side.LEFT, state, worldIn, pos);
        byte right = MultipartUtils.getRedstonePower(Side.RIGHT, state, worldIn, pos);
        byte back = MultipartUtils.getRedstonePower(Side.BACK, state, worldIn, pos);
        map.put(Side.LEFT, left);
        map.put(Side.RIGHT, right);
        map.put(Side.BACK, back);
        map.put(Side.FRONT, back > 0 ? left : right);
        return map;
    }
}
