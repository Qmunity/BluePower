package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class WorldGenFlowers {

    public static void registerFlowers(RegistryEvent.Register<Feature<?>> event) {
        ConfiguredFeature<?, ?> feature = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME,
                new ConfiguredFeature<>(Feature.FLOWER, new RandomPatchConfiguration(2, 2, 2,PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BPBlocks.indigo_flower)))))
        );
        PlacementUtils.register("bluepower:" + Refs.INDIGOFLOWER_NAME, Holder.direct(feature), RarityFilter.onAverageOnceEvery(BPConfig.CONFIG.flowerSpawnChance.get()/2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        if(event.getName() != null && event.getName().toString().contains("forest")){
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            PlacedFeature feature = BuiltinRegistries.PLACED_FEATURE.get(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME));
            if(feature != null && BPConfig.CONFIG.flowerSpawnChance.get() > 0)
                generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Holder.direct(feature));
        }
    }

}
