package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT;
    }

    @Override
    public Map<Side, Byte> getSidePower(BlockState state, TileGate gate){
        Map<Side, Byte> map = new HashMap<>();
        Direction sideLeft = toDirection(Side.LEFT, state);
        Direction sideRight = toDirection(Side.RIGHT, state);
        Direction sideBack = toDirection(Side.BACK, state);
        Level worldIn = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        byte left = gate.isDisabled(Side.LEFT) ? 16 : (byte) worldIn.getSignal(pos.relative(sideLeft), sideLeft);
        byte right = gate.isDisabled(Side.RIGHT) ? 16 : (byte) worldIn.getSignal(pos.relative(sideRight), sideRight);
        byte back = gate.isDisabled(Side.BACK) ? 16 : (byte) worldIn.getSignal(pos.relative(sideBack), sideBack);
        map.put(Side.LEFT, left);
        map.put(Side.RIGHT, right);
        map.put(Side.BACK, back);
        map.put(Side.FRONT, computeRedstone(Side.FRONT, back, (byte) 0, left, right));
        return map;
    }

    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right){
        boolean and = left > 0 && right > 0 && back > 0;
        return (byte) (and != inverted ? 16 : 0);
    }

    @Override
    protected void cycleDisabledStates(BlockState state, TileGate gate) {
        boolean left = gate.isDisabled(Side.LEFT), right = gate.isDisabled(Side.RIGHT), back = gate.isDisabled(Side.BACK);
        if (!left && !back && !right) {
            gate.setDisabled(Side.RIGHT, true);
        } else if (!left && !back) {
            gate.setDisabled(Side.BACK, true);
            gate.setDisabled(Side.RIGHT, false);
        } else if (!left && !right) {
            gate.setDisabled(Side.LEFT, true);
            gate.setDisabled(Side.BACK, false);
        } else if (!back && !right) {
            gate.setDisabled(Side.LEFT, false);
            gate.setDisabled(Side.BACK, true);
            gate.setDisabled(Side.RIGHT, true);
        } else if (!left) {
            gate.setDisabled(Side.LEFT, true);
            gate.setDisabled(Side.BACK, false);
        } else if (!back) {
            gate.setDisabled(Side.BACK, true);
            gate.setDisabled(Side.RIGHT, false);
        } else {// right enabled
            gate.setDisabled(Side.LEFT, false);
            gate.setDisabled(Side.BACK, false);
        }
    }
}
