package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
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
        Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> feature = FeatureUtils.register( "bluepower:" + Refs.INDIGOFLOWER_NAME, Feature.FLOWER, new RandomPatchConfiguration(16, BPConfig.CONFIG.flowerSpawnChance.get(), BPConfig.CONFIG.flowerSpawnChance.get(),PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BPBlocks.indigo_flower.get())))));
        PlacementUtils.register("bluepower:" + Refs.INDIGOFLOWER_NAME, feature, RarityFilter.onAverageOnceEvery(5-BPConfig.CONFIG.flowerSpawnChance.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

}
