package com.bluepowermod.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

public class WorldGenRubberTree extends TreeFeature {

    public WorldGenRubberTree(Codec<BaseTreeFeatureConfig> codec) {
        super(codec);
    }

}
