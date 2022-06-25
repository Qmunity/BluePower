package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

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
