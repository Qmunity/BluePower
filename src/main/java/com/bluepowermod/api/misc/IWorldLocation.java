package com.bluepowermod.api.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IWorldLocation {

    BlockPos getBlockPos();
    Level getLevel();

}
