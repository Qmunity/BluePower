package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class BlockRubberLog extends RotatedPillarBlock {
    public BlockRubberLog(Properties properties){
        super(properties);
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLOG_NAME);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(AXIS);
    }

}
