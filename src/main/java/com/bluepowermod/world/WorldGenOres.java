package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class WorldGenOres {

    public static void registerOres() {
        if (BPConfig.CONFIG.generateAmethyst.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountAmethyst.get(), BPConfig.CONFIG.veinSizeAmethyst.get(), BPConfig.CONFIG.minAmethystY.get(), BPConfig.CONFIG.maxAmethystY.get(), BPBlocks.amethyst_ore.get(), BPBlocks.amethyst_deepslate.get());
        }
        if (BPConfig.CONFIG.generateRuby.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountRuby.get(), BPConfig.CONFIG.veinSizeRuby.get(), BPConfig.CONFIG.minRubyY.get(), BPConfig.CONFIG.maxRubyY.get(), BPBlocks.ruby_ore.get(), BPBlocks.ruby_deepslate.get());
        }
        if (BPConfig.CONFIG.generateSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSapphire.get(), BPConfig.CONFIG.veinSizeSapphire.get(), BPConfig.CONFIG.minSapphireY.get(), BPConfig.CONFIG.maxSapphireY.get(), BPBlocks.sapphire_ore.get(), BPBlocks.sapphire_deepslate.get());
        }
        if (BPConfig.CONFIG.generateGreenSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountGreenSapphire.get(), BPConfig.CONFIG.veinSizeGreenSapphire.get(), BPConfig.CONFIG.minGreenSapphireY.get(), BPConfig.CONFIG.maxGreenSapphireY.get(), BPBlocks.green_sapphire_ore.get(), BPBlocks.green_sapphire_deepslate.get());
        }
        if (BPConfig.CONFIG.generateSilver.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSilver.get(), BPConfig.CONFIG.veinSizeSilver.get(), BPConfig.CONFIG.minSilverY.get(), BPConfig.CONFIG.maxSilverY.get(), BPBlocks.silver_ore.get(), BPBlocks.silver_deepslate.get());
        }
        if (BPConfig.CONFIG.generateTeslatite.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTeslatite.get(), BPConfig.CONFIG.veinSizeTeslatite.get(), BPConfig.CONFIG.minTeslatiteY.get(), BPConfig.CONFIG.maxTeslatiteY.get(), BPBlocks.teslatite_ore.get(), BPBlocks.teslatite_deepslate.get());
        }
        if (BPConfig.CONFIG.generateZinc.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountZinc.get(), BPConfig.CONFIG.veinSizeZinc.get(), BPConfig.CONFIG.minZincY.get(), BPConfig.CONFIG.maxZincY.get(), BPBlocks.zinc_ore.get(), BPBlocks.zinc_deepslate.get());
        }
        if (BPConfig.CONFIG.generateTungsten.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTungsten.get(), BPConfig.CONFIG.veinSizeTungsten.get(), BPConfig.CONFIG.minTungstenY.get(), BPConfig.CONFIG.maxTungstenY.get(), BPBlocks.tungsten_ore.get(), BPBlocks.tungsten_deepslate.get());
        }
    }

    private static void registerConfiguredOre(int veinCount, int veinSize, int minY, int maxY, Block ore, Block deepSlate){
        List<OreConfiguration.TargetBlockState> oreTarget = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ore.defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepSlate.defaultBlockState()));
        Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeature = FeatureUtils.register(ForgeRegistries.BLOCKS.getResourceKey(ore).toString(), Feature.ORE, new OreConfiguration(oreTarget, veinSize));
        PlacementUtils.register(ForgeRegistries.BLOCKS.getResourceKey(ore).toString(), configuredFeature, commonOrePlacement(veinCount, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(minY), VerticalAnchor.aboveBottom(maxY + 64))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(i), modifier);
    }

    public void onBiomeLoad(){
        //if(!event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            //BiomeGenerationSettingsBuilder generation = event.getGeneration();
            if (BPConfig.CONFIG.generateAmethyst.get()) {
                //PlacedFeature amethyst_feature = BuiltinRegistries.PLACED_FEATURE.get(ForgeRegistries.BLOCKS.getResourceKey(BPBlocks.amethyst_ore));
                //if(amethyst_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(amethyst_feature));
            }
            if (BPConfig.CONFIG.generateRuby.get()) {
                //PlacedFeature ruby_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.ruby_ore.getRegistryName());
                //if(ruby_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(ruby_feature));
            }
            if (BPConfig.CONFIG.generateSapphire.get()) {
                //PlacedFeature sapphire_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.sapphire_ore.getRegistryName());
                //if(sapphire_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(sapphire_feature));
            }
            if (BPConfig.CONFIG.generateGreenSapphire.get()) {
                //PlacedFeature green_sapphire_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.green_sapphire_ore.getRegistryName());
                //if(green_sapphire_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(green_sapphire_feature));
            }
            if (BPConfig.CONFIG.generateSilver.get()) {
                //PlacedFeature silver_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.silver_ore.getRegistryName());
                //if(silver_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(silver_feature));
            }
            if (BPConfig.CONFIG.generateTeslatite.get()) {
                //PlacedFeature teslatite_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.teslatite_ore.getRegistryName());
                //if(teslatite_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(teslatite_feature));
            }
            if (BPConfig.CONFIG.generateZinc.get()) {
                //PlacedFeature zinc_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.zinc_ore.getRegistryName());
                //if(zinc_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(zinc_feature));
            }
            if (BPConfig.CONFIG.generateTungsten.get()) {
                //PlacedFeature tungsten_feature = BuiltinRegistries.PLACED_FEATURE.get(BPBlocks.tungsten_ore.getRegistryName());
                //if(tungsten_feature != null)
                    //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(tungsten_feature));
            }
        }
    //}
}
