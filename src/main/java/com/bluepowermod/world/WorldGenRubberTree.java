package com.bluepowermod.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

public class WorldGenRubberTree extends TreeFeature {

    public WorldGenRubberTree(Codec<TreeConfiguration> codec) {
        super(codec);
    }

}
