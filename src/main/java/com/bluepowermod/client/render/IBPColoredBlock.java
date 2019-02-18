package com.bluepowermod.client.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author MoreThanHidden
 */
public interface IBPColoredBlock {

    int getColor(IBlockAccess w, BlockPos pos, int tint);
    int getColor(int tint);

}
