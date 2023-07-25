package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import net.minecraft.world.level.block.LeavesBlock;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockRubberLeaves extends LeavesBlock {

    public BlockRubberLeaves(Properties properties){
        super(properties);
        BPBlocks.blockList.add(this);
    }

}
