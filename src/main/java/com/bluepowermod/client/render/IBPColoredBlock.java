package com.bluepowermod.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredBlock {

    int getColor(BlockState state, IBlockReader w, BlockPos pos, int tint);
    int getColor(ItemStack stack, int tint);

}
