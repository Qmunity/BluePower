package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class WorldGenFlowers {

    public static void registerFlowers() {
        ConfiguredFeature<?, ?> feature = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:" + Refs.INDIGOFLOWER_NAME,
                new ConfiguredFeature<>(Feature.FLOWER, new RandomPatchConfiguration(2, 2, 2,PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BPBlocks.indigo_flower.get())))))
        );
        PlacementUtils.register("bluepower:" + Refs.INDIGOFLOWER_NAME, Holder.direct(feature), RarityFilter.onAverageOnceEvery(BPConfig.CONFIG.flowerSpawnChance.get()/2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    public void onBiomeLoad(){
        // if(event.getName() != null && event.getName().toString().contains("forest")){
            //BiomeGenerationSettingsBuilder generation = event.getGeneration();
            //PlacedFeature feature = BuiltinRegistries.PLACED_FEATURE.get(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME));
            // if(feature != null && BPConfig.CONFIG.flowerSpawnChance.get() > 0)
            //    generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Holder.direct(feature));
        // }
    }

}
