package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class WorldGenFlowers {

    static final FlowersFeature INDIGO_FLOWER = new FlowersFeature(NoFeatureConfig::deserialize) {
        @Override
        public BlockState getRandomFlower(Random random, BlockPos blockPos) {
            return BPBlocks.indigo_flower.getDefaultState();
        }
    };

    static void setupFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {

            int n = 0;
            if (biome == Biomes.BIRCH_FOREST)
                n = 1;
            else if (biome == Biomes.BIRCH_FOREST_HILLS)
                n = 1;
            else if (biome == Biomes.PLAINS)
                n = 1;
            else if (biome == Biomes.FOREST)
                n = 4;
            else if (biome == Biomes.DARK_FOREST)
                n = 4;

            //biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(INDIGO_FLOWER, );
        }
    }

}
