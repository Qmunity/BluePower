package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGateBuffer extends BlockGateNot{
    @Override
    protected byte getOutput(byte back) {
        return (byte) (back > 0 ? 16 : 0);
    }


    @Override
    protected int getDelay(BlockState state, TileGate gate) {
        return 2;
    }

    @Override
    protected boolean cycleDisabledStates(BlockState state, TileGate gate) {
        boolean left = gate.isDisabled(Side.LEFT), right = gate.isDisabled(Side.RIGHT);
        if (!left && !right) {
            gate.setDisabled(Side.RIGHT, true);
        } else if (!left) {
            gate.setDisabled(Side.LEFT, true);
            gate.setDisabled(Side.RIGHT, false);
        } else if (!right){
            gate.setDisabled(Side.RIGHT, true);
        }else {// right enabled
            gate.setDisabled(Side.LEFT, false);
            gate.setDisabled(Side.RIGHT, false);
        }
        return true;
    }
}
