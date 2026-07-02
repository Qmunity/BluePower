package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateAnd extends BlockGateLogic {
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
        Level worldIn = gate.getLevel();
        BlockPos pos = gate.getBlockPos();
        byte left = MultipartUtils.getRedstonePower(Side.LEFT, state, worldIn, pos);
        byte right = MultipartUtils.getRedstonePower(Side.RIGHT, state, worldIn, pos);
        byte back = MultipartUtils.getRedstonePower(Side.BACK, state, worldIn, pos);
        map.put(Side.LEFT, left);
        map.put(Side.RIGHT, right);
        map.put(Side.BACK, back);
        map.put(Side.FRONT, computeRedstone(Side.FRONT, back, (byte) 0, left, right, gate));
        return map;
    }

    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right, TileGate gate){
        boolean and = (left > 0 || gate.isDisabled(Side.LEFT)) &&
                (right > 0 || gate.isDisabled(Side.RIGHT)) &&
                (back > 0 || gate.isDisabled(Side.BACK));
        return (byte) (and != inverted ? 16 : 0);
    }

    @Override
    protected boolean cycleDisabledStates(BlockState state, TileGate gate) {
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
        return true;
    }

    @Override
    protected void onBlockPlace(BlockState state, TileGate gate) {
        if (inverted) gate.setPowered(Side.FRONT, true);
    }
}
