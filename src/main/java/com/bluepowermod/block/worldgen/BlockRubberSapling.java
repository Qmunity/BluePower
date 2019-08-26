package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.StateContainer;

import java.util.Properties;
import java.util.Random;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling(Tree tree, Properties properties) {
        super(tree, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, 0));
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERSAPLING_NAME);
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(STAGE);
    }

}
