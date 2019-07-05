package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class WorldGenFlowers {

    static final FlowersFeature INDIGO_FLOWER = new FlowersFeature(NoFeatureConfig::deserialize) {
        @Override
        public BlockState getRandomFlower(Random random, BlockPos blockPos) {
            return BPBlocks.indigo_flower.getDefaultState();
        }
    };

    public static void setupFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {

            int n = 0;
            if (biome == Biomes.BIRCH_FOREST)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.BIRCH_FOREST_HILLS)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.PLAINS)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.FOREST)
                n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.DARK_FOREST)
                n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(WorldGenFlowers.INDIGO_FLOWER, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(n)));
        }
    }

}
