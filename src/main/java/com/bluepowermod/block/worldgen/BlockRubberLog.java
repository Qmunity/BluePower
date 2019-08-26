package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class BlockRubberLog extends LogBlock {
    public BlockRubberLog(Properties properties){
        super(MaterialColor.BROWN, properties);
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLOG_NAME);
        this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(AXIS);
    }

}
