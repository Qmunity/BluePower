package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.HeightmapConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class WorldGenFlowers {

    public static void initFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {
            int n = getConfigAmount(biome.getRegistryName());
            if(n > 0) {
                RandomPatchConfiguration featureConfig = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BPBlocks.indigo_flower.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(64).build();
                Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME + n, Feature.FLOWER.configured(featureConfig).decorated(FeatureDecorator.SPREAD_32_ABOVE.configured(NoneDecoratorConfiguration.INSTANCE)).decorated(FeatureDecorator.HEIGHTMAP.configured(new HeightmapConfiguration(Heightmap.Types.MOTION_BLOCKING))).count(n));
            }
        }
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        int n = getConfigAmount(event.getName());
        if(n > 0) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            ConfiguredFeature<?,?> feature = BuiltinRegistries.CONFIGURED_FEATURE.get(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME + n));
            if(feature != null)
                generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, feature);
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
