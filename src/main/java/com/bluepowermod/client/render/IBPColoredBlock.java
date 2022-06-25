package com.bluepowermod.client.render;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredBlock {

    int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint);
    int getColor(ItemStack stack, int tint);

}
