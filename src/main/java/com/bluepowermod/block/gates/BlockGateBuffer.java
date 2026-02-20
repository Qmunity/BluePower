package com.bluepowermod.block.gates;

public class BlockGateBuffer extends BlockGateNot{
    @Override
    protected byte getOutput(byte back) {
        return (byte) (back > 0 ? 16 : 0);
    }
}
