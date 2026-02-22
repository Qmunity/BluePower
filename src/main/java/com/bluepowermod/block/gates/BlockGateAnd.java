package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
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

    public Map<Side, Byte> getSidePower(BlockGetter worldIn, BlockState state, BlockPos pos){
        Map<Side, Byte> map = new HashMap<>();
        Direction[] dirs = DirectionHelper.ArrayFromDirection(state.getValue(FACING));
        Direction side_left = dirs[state.getValue(ROTATION) == 3 ? 0 : state.getValue(ROTATION) + 1];
        Direction side_right = side_left.getOpposite();
        Direction side_back = dirs[state.getValue(ROTATION)];
        BlockPos pos_left = pos.relative(side_left);
        BlockPos pos_right = pos.relative(side_right);
        BlockPos pos_back = pos.relative(side_back);
        BlockState state_left = worldIn.getBlockState(pos_left);
        BlockState state_right = worldIn.getBlockState(pos_right);
        BlockState state_back = worldIn.getBlockState(pos_back);
        byte left = (byte) state_left.getSignal(worldIn, pos_left, side_right);
        byte right = (byte) state_right.getSignal(worldIn, pos_right, side_left);
        byte back = (byte) state_back.getSignal(worldIn, pos_back, side_back.getOpposite());
        if(state_left.getBlock() instanceof RedStoneWireBlock){left = state_left.getValue(RedStoneWireBlock.POWER).byteValue();}
        if(state_right.getBlock() instanceof RedStoneWireBlock){right = state_right.getValue(RedStoneWireBlock.POWER).byteValue();}
        if(state_back.getBlock() instanceof RedStoneWireBlock){back = state_back.getValue(RedStoneWireBlock.POWER).byteValue();}
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
