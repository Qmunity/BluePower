package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling(Properties properties) {
        super(TreeGrower.OAK, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
        BPBlocks.blockList.add(this);
    }

}
