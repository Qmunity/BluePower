package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockRubberLog extends RotatedPillarBlock {
    public BlockRubberLog(Properties properties){
        super(properties);
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLOG_NAME);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(AXIS);
    }

}
