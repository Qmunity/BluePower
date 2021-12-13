package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import java.util.Objects;

public class WorldGenFlowers {

    public static void initFlowers(){
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME,
                Feature.FLOWER.configured(new RandomPatchConfiguration(64, 6, 2,
                        () -> Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BPBlocks.indigo_flower))).onlyWhenEmpty())
                )
        );
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        int n = getConfigAmount(Objects.requireNonNull(event.getName()));
        if(n > 0) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            ConfiguredFeature<?, ?> feature = BuiltinRegistries.CONFIGURED_FEATURE.get(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME));
            if(feature != null)
                generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, feature.placed(RarityFilter.onAverageOnceEvery(n), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        }
    }

    static int getConfigAmount(ResourceLocation biome){
        int n = 0;
        if (biome.equals(Biomes.BIRCH_FOREST.getRegistryName()))
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
