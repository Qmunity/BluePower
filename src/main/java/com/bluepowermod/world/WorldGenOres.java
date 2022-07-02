package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class WorldGenOres {

    public static void registerOres() {
        if (BPConfig.CONFIG.generateAmethyst.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountAmethyst.get(), BPConfig.CONFIG.veinSizeAmethyst.get(), BPConfig.CONFIG.minAmethystY.get(), BPConfig.CONFIG.maxAmethystY.get(), BPBlocks.amethyst_ore.get(), BPBlocks.amethyst_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.amethyst_ore.get(), BPBlocks.amethyst_deepslate.get());
        }
        if (BPConfig.CONFIG.generateRuby.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountRuby.get(), BPConfig.CONFIG.veinSizeRuby.get(), BPConfig.CONFIG.minRubyY.get(), BPConfig.CONFIG.maxRubyY.get(), BPBlocks.ruby_ore.get(), BPBlocks.ruby_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.ruby_ore.get(), BPBlocks.ruby_deepslate.get());
        }
        if (BPConfig.CONFIG.generateSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSapphire.get(), BPConfig.CONFIG.veinSizeSapphire.get(), BPConfig.CONFIG.minSapphireY.get(), BPConfig.CONFIG.maxSapphireY.get(), BPBlocks.sapphire_ore.get(), BPBlocks.sapphire_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.sapphire_ore.get(), BPBlocks.sapphire_deepslate.get());
        }
        if (BPConfig.CONFIG.generateGreenSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountGreenSapphire.get(), BPConfig.CONFIG.veinSizeGreenSapphire.get(), BPConfig.CONFIG.minGreenSapphireY.get(), BPConfig.CONFIG.maxGreenSapphireY.get(), BPBlocks.green_sapphire_ore.get(), BPBlocks.green_sapphire_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.green_sapphire_ore.get(), BPBlocks.green_sapphire_deepslate.get());
        }
        if (BPConfig.CONFIG.generateSilver.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSilver.get(), BPConfig.CONFIG.veinSizeSilver.get(), BPConfig.CONFIG.minSilverY.get(), BPConfig.CONFIG.maxSilverY.get(), BPBlocks.silver_ore.get(), BPBlocks.silver_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.silver_ore.get(), BPBlocks.silver_deepslate.get());
        }
        if (BPConfig.CONFIG.generateTeslatite.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTeslatite.get(), BPConfig.CONFIG.veinSizeTeslatite.get(), BPConfig.CONFIG.minTeslatiteY.get(), BPConfig.CONFIG.maxTeslatiteY.get(), BPBlocks.teslatite_ore.get(), BPBlocks.teslatite_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.teslatite_ore.get(), BPBlocks.teslatite_deepslate.get());
        }
        if (BPConfig.CONFIG.generateZinc.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountZinc.get(), BPConfig.CONFIG.veinSizeZinc.get(), BPConfig.CONFIG.minZincY.get(), BPConfig.CONFIG.maxZincY.get(), BPBlocks.zinc_ore.get(), BPBlocks.zinc_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.zinc_ore.get(), BPBlocks.zinc_deepslate.get());
        }
        if (BPConfig.CONFIG.generateTungsten.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTungsten.get(), BPConfig.CONFIG.veinSizeTungsten.get(), BPConfig.CONFIG.minTungstenY.get(), BPConfig.CONFIG.maxTungstenY.get(), BPBlocks.tungsten_ore.get(), BPBlocks.tungsten_deepslate.get());
        }else{
            registerPlaceholder(BPBlocks.tungsten_ore.get(), BPBlocks.tungsten_deepslate.get());
        }
    }

    //Register Placeholder Only to Avoid Error from the Biome Modifiers
    private static void registerPlaceholder(Block ore, Block deepSlate) {
        registerConfiguredOre(0, 0, 0, 0, ore, deepSlate);
    }

    private static void registerConfiguredOre(int veinCount, int veinSize, int minY, int maxY, Block ore, Block deepSlate){
        List<OreConfiguration.TargetBlockState> oreTarget = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ore.defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepSlate.defaultBlockState()));
        Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeature = FeatureUtils.register(ForgeRegistries.BLOCKS.getResourceKey(ore).get().location().toString(), Feature.ORE, new OreConfiguration(oreTarget, veinSize));
        PlacementUtils.register(ForgeRegistries.BLOCKS.getResourceKey(ore).get().location().toString(), configuredFeature, commonOrePlacement(veinCount, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(minY), VerticalAnchor.aboveBottom(maxY + 64))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(i), modifier);
    }

}
