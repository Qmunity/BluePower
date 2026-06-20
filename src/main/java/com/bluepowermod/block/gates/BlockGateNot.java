package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateNot extends BlockGateBase{
    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        Map<Side, Byte> map = new HashMap<>();
        byte back = (byte) gate.getLevel().getSignal(gate.getBlockPos().relative(toDirection(Side.BACK, state)), toDirection(Side.BACK, state));
        byte output = getOutput(back);
        map.put(Side.FRONT, output);
        map.put(Side.LEFT, output);
        map.put(Side.RIGHT, output);
        map.put(Side.BACK, back);
        return map;
    }


    @Override
    protected boolean cycleDisabledStates(BlockState state, TileGate gate) {
        boolean left = gate.isDisabled(Side.LEFT), right = gate.isDisabled(Side.RIGHT), front = gate.isDisabled(Side.FRONT);
        if (!left && !front && !right) {
            gate.setDisabled(Side.RIGHT, true);
        } else if (!left && !front) {
            gate.setDisabled(Side.FRONT, true);
            gate.setDisabled(Side.RIGHT, false);
        } else if (!left && !right) {
            gate.setDisabled(Side.LEFT, true);
            gate.setDisabled(Side.FRONT, false);
        } else if (!front && !right) {
            gate.setDisabled(Side.LEFT, false);
            gate.setDisabled(Side.FRONT, true);
            gate.setDisabled(Side.RIGHT, true);
        } else if (!left) {
            gate.setDisabled(Side.LEFT, true);
            gate.setDisabled(Side.FRONT, false);
        } else if (!front) {
            gate.setDisabled(Side.FRONT, true);
            gate.setDisabled(Side.RIGHT, false);
        } else {// right enabled
            gate.setDisabled(Side.LEFT, false);
            gate.setDisabled(Side.FRONT, false);
        }
        return true;
    }

    protected byte getOutput(byte back){
        return (byte) (back > 0 ? 0 : 16);
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return (side == Side.FRONT || side == Side.LEFT || side == Side.RIGHT) && !gate.isDisabled(side);
    }
}
