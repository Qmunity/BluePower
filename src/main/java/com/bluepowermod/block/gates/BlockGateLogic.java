package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class BlockGateLogic extends BlockGateBase {
    protected abstract Map<Side, Byte> getSidePower(BlockState state, TileGate gate);


    @Override
    protected boolean checkPower(BlockState state, TileGate gate, boolean onTick){
        Map<Side, Byte> map = getSidePower(state, gate);
        return gate.updateStates(map, true);
    }
}
