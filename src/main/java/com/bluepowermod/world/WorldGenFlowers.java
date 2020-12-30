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
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGenFlowers {

    public static void initFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {
            int n = getConfigAmount(biome.getRegistryName());
            if(n > 0) {
                BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(BPBlocks.indigo_flower.getDefaultState(), 2), new SimpleBlockPlacer())).tries(64).build();
                Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME + n, Feature.FLOWER.withConfiguration(featureConfig).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(n));
            }
        }
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        int n = getConfigAmount(event.getName());
        if(n > 0) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            ConfiguredFeature<?,?> feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME + n));
            if(feature != null)
                generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, feature);
        }
    }

    static int getConfigAmount(ResourceLocation biome){
        int n = 0;
        if (biome.equals(Biomes.BIRCH_FOREST.getLocation()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.BIRCH_FOREST_HILLS.getLocation()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.PLAINS.getLocation()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.FOREST.getLocation()))
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.DARK_FOREST.getLocation()))
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        return n;
    }

}
