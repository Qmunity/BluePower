package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.registries.ForgeRegistries;

import static com.bluepowermod.world.BPWorldGen.addFeatureToBiome;

public class WorldGenFlowers {

    public static void initFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {
            int n = getConfigAmount(ForgeRegistries.BIOMES.getKey(biome));
            if(n > 0) {
                BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(BPBlocks.indigo_flower.getDefaultState(), 2), new SimpleBlockPlacer())).tries(64).build();
                Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME + n, Feature.FLOWER.withConfiguration(featureConfig).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(n));
            }
        }
    }
    public static void setupFlowers() {
        for(Biome biome : ForgeRegistries.BIOMES) {
            int n = getConfigAmount(ForgeRegistries.BIOMES.getKey(biome));
            if(n > 0) {
                addFeatureToBiome(biome, GenerationStage.Decoration.VEGETAL_DECORATION, WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME + n)));
            }
        }
    }

    static int getConfigAmount(ResourceLocation biome){
        int n = 0;
        if (biome == Biomes.BIRCH_FOREST.getRegistryName())
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome == Biomes.BIRCH_FOREST_HILLS.getRegistryName())
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome == Biomes.PLAINS.getRegistryName())
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome == Biomes.FOREST.getRegistryName())
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome == Biomes.DARK_FOREST.getRegistryName())
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        return n;
    }

}
