package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateAnd extends BlockGateBase {
    protected final boolean inverted;

    public BlockGateAnd(boolean inverted){
        this.inverted = inverted;
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, BlockGetter blockAccess, BlockPos pos) {
        return side == Side.FRONT;
    }

    @Override
    public Map<Side, Byte> getSidePower(SignalGetter worldIn, BlockState state, BlockPos pos){
        Map<Side, Byte> map = new HashMap<>();
        Direction sideLeft = toDirection(Side.LEFT, state);
        Direction sideRight = toDirection(Side.RIGHT, state);
        Direction sideBack = toDirection(Side.BACK, state);
        byte left = (byte) worldIn.getSignal(pos.relative(sideLeft), sideLeft);
        byte right = (byte) worldIn.getSignal(pos.relative(sideRight), sideRight);
        byte back = (byte) worldIn.getSignal(pos.relative(sideBack), sideBack);
        map.put(Side.LEFT, left);
        map.put(Side.RIGHT, right);
        map.put(Side.BACK, back);
        map.put(Side.FRONT, computeRedstone(Side.FRONT, back, (byte) 0, left, right));
        return map;
    }

    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right){
        boolean and = left > 0 && right > 0 && back > 0;
        return (byte) (and != inverted ? 16 : 0);
    }}
