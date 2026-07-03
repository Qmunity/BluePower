package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.tile.tier1.gate.TileRandomizer;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockGateRandomizer extends BlockGateBase{
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileRandomizer(blockPos, blockState);
    }

    @Override
    public boolean ticks() {
        return true;
    }

    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side != Side.BACK;
    }

    @Override
    protected boolean checkPower(BlockState state, TileGate gate, boolean onTick) {
        boolean oldInput = gate.isPowered(Side.BACK);
        boolean in = MultipartUtils.getRedstonePower(Side.BACK, state, gate.getLevel(), gate.getBlockPos()) > 0;
        if (oldInput != in) gate.setPowered(Side.BACK, in);
        return false;
    }
}
