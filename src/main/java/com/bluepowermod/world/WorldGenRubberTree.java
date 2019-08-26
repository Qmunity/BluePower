package com.bluepowermod.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class WorldGenRubberTree extends AbstractTreeFeature {


    public WorldGenRubberTree(Function p_i49920_1_, boolean p_i49920_2_) {
        super(p_i49920_1_, p_i49920_2_);
    }

    @Override
    protected boolean place(Set set, IWorldGenerationReader iWorldGenerationReader, Random random, BlockPos blockPos, MutableBoundingBox mutableBoundingBox) {
        return false;
    }
}
