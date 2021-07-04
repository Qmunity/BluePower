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
                BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).add(BPBlocks.indigo_flower.defaultBlockState(), 2), new SimpleBlockPlacer())).tries(64).build();
                Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME + n, Feature.FLOWER.configured(featureConfig).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP).chance(n));
            }
        }
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        int n = getConfigAmount(event.getName());
        if(n > 0) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            ConfiguredFeature<?,?> feature = WorldGenRegistries.CONFIGURED_FEATURE.get(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME + n));
            if(feature != null)
                generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, feature);
        }
    }

    static int getConfigAmount(ResourceLocation biome){
        int n = 0;
        if (biome.equals(Biomes.BIRCH_FOREST.getRegistryName()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.BIRCH_FOREST_HILLS.getRegistryName()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.PLAINS.getRegistryName()))
            n = BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.FOREST.getRegistryName()))
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        else if (biome.equals(Biomes.DARK_FOREST.getRegistryName()))
            n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
        return n;
    }

}
