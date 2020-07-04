package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGenFlowers {

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

            BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(BPBlocks.indigo_flower.getDefaultState(), 2), new SimpleBlockPlacer())).tries(64).build();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(featureConfig).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(n))));
        }
    }

}
