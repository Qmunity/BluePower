package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateNot extends BlockGateBase{
    @Override
    protected Map<Side, Byte> getSidePower(BlockGetter worldIn, BlockState state, BlockPos pos) {
        Map<Side, Byte> map = new HashMap<>();
        Direction[] dirs = DirectionHelper.ArrayFromDirection(state.getValue(FACING));
        Direction sideBack = dirs[state.getValue(ROTATION)];
        BlockPos posBack = pos.relative(sideBack);
        BlockState stateBack = worldIn.getBlockState(posBack);
        byte back = (byte) stateBack.getSignal(worldIn, posBack, sideBack.getOpposite());
        if(stateBack.getBlock() instanceof RedStoneWireBlock){back = stateBack.getValue(RedStoneWireBlock.POWER).byteValue();}
        byte output = getOutput(back);
        map.put(Side.FRONT, output);
        map.put(Side.LEFT, output);
        map.put(Side.RIGHT, output);
        map.put(Side.BACK, back);
        return map;
    }

    protected byte getOutput(byte back){
        return (byte) (back > 0 ? 0 : 16);
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, BlockGetter blockAccess, BlockPos pos) {
        return side == Side.FRONT || side == Side.LEFT || side == Side.RIGHT;
    }
}
