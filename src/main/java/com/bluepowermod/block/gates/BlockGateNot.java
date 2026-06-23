package com.bluepowermod.block.gates;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockGateNot extends BlockGateLogic {
    @Override
    protected Map<Side, Byte> getSidePower(BlockState state, TileGate gate) {
        Map<Side, Byte> map = new HashMap<>();
        byte back = MultipartUtils.getRedstonePower(Side.BACK, state, gate.getLevel(), gate.getBlockPos());
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

    @Override
    protected void onBlockPlace(BlockState state, TileGate gate) {
        if (this == BPBlocks.blockGateNOT.get()) {
            gate.setPowered(Side.FRONT, true);
            gate.setPowered(Side.LEFT, true);
            gate.setPowered(Side.RIGHT, true);
        }
    }
}
