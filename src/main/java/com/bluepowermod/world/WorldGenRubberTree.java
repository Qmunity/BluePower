package com.bluepowermod.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class WorldGenRubberTree extends AbstractTreeFeature {


    public WorldGenRubberTree(Function p_i225797_1_) {
        super(p_i225797_1_);
    }

    @Override
    protected boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos positionIn, Set p_225557_4_, Set p_225557_5_, MutableBoundingBox boundingBoxIn, BaseTreeFeatureConfig configIn) {
        return false;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator generator, Random rand, BlockPos pos, IFeatureConfig config) {
        return false;
    }
}
