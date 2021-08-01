package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.state.StateContainer;

import java.util.Properties;
import java.util.Random;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling(AbstractTreeGrower tree, Properties properties) {
        super(tree, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERSAPLING_NAME);
        BPBlocks.blockList.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(STAGE);
    }

}
