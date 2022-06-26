package com.bluepowermod.tile.tier2;

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileTube extends TileBase {
    public TileTube(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.TUBE.get(), pos, state);
    }
}
