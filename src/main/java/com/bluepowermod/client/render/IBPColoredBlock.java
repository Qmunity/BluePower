package com.bluepowermod.client.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredBlock {

    int getColor(IBlockReader w, BlockPos pos, int tint);
    int getColor(int tint);

}
