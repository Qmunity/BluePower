package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.state.BlockStateContainer;

public class BlockRubberLog extends LogBlock {
    public BlockRubberLog(){
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERLOG_NAME);
        this.setDefaultState(this.blockState.getBaseState().with(LOG_AXIS, EnumAxis.Y));
        this.setTranslationKey(Refs.MODID + ":" + Refs.RUBBERLOG_NAME);
        this.setCreativeTab(BPCreativeTabs.blocks);
        BPBlocks.blockList.add(this);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public BlockState getStateFromMeta(int meta){
        BlockState iblockstate = this.getDefaultState();

        switch (meta & 12){
            case 0:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.with(LOG_AXIS, EnumAxis.NONE);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    @SuppressWarnings("incomplete-switch")
    public int getMetaFromState(BlockState state){
        int i = 0;
        switch (state.get(LOG_AXIS)) {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, LOG_AXIS);
    }

}
