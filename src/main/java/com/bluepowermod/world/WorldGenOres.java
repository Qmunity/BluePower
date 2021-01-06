package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenOres {

    public static void initOres(){
        if (BPConfig.CONFIG.generateAmethyst.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountAmethyst.get(), BPConfig.CONFIG.veinSizeAmethyst.get(), BPConfig.CONFIG.minAmethystY.get(), BPConfig.CONFIG.maxAmethystY.get(), BPBlocks.amethyst_ore);
        }
        if (BPConfig.CONFIG.generateRuby.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountRuby.get(), BPConfig.CONFIG.veinSizeRuby.get(), BPConfig.CONFIG.minRubyY.get(), BPConfig.CONFIG.maxRubyY.get(), BPBlocks.ruby_ore);
        }
        if (BPConfig.CONFIG.generateSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSapphire.get(), BPConfig.CONFIG.veinSizeSapphire.get(), BPConfig.CONFIG.minSapphireY.get(), BPConfig.CONFIG.maxSapphireY.get(), BPBlocks.sapphire_ore);
        }
        if (BPConfig.CONFIG.generateGreenSapphire.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountGreenSapphire.get(), BPConfig.CONFIG.veinSizeGreenSapphire.get(), BPConfig.CONFIG.minGreenSapphireY.get(), BPConfig.CONFIG.maxGreenSapphireY.get(), BPBlocks.green_sapphire_ore);
        }
        if (BPConfig.CONFIG.generateSilver.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountSilver.get(), BPConfig.CONFIG.veinSizeSilver.get(), BPConfig.CONFIG.minSilverY.get(), BPConfig.CONFIG.maxSilverY.get(), BPBlocks.silver_ore);
        }
        if (BPConfig.CONFIG.generateTeslatite.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTeslatite.get(), BPConfig.CONFIG.veinSizeTeslatite.get(), BPConfig.CONFIG.minTeslatiteY.get(), BPConfig.CONFIG.maxTeslatiteY.get(), BPBlocks.teslatite_ore);
        }
        if (BPConfig.CONFIG.generateZinc.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountZinc.get(), BPConfig.CONFIG.veinSizeZinc.get(), BPConfig.CONFIG.minZincY.get(), BPConfig.CONFIG.maxZincY.get(), BPBlocks.zinc_ore);
        }
        if (BPConfig.CONFIG.generateCopper.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountCopper.get(), BPConfig.CONFIG.veinSizeCopper.get(), BPConfig.CONFIG.minCopperY.get(), BPConfig.CONFIG.maxCopperY.get(), BPBlocks.copper_ore);
        }
        if (BPConfig.CONFIG.generateTungsten.get()) {
            registerConfiguredOre(BPConfig.CONFIG.veinCountTungsten.get(), BPConfig.CONFIG.veinSizeTungsten.get(), BPConfig.CONFIG.minTungstenY.get(), BPConfig.CONFIG.maxTungstenY.get(), BPBlocks.tungsten_ore);
        }
    }

    private static void registerConfiguredOre(int veinCount, int veinSize, int minY, int maxY, Block ore){
        if(ore.getRegistryName() != null)
         Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getRegistryName(), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ore.getDefaultState(), veinSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minY, minY, maxY))).square().func_242731_b(veinCount));
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        if(!event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND)) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            if (BPConfig.CONFIG.generateAmethyst.get()) {
                ConfiguredFeature<?,?> amethyst_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.amethyst_ore.getRegistryName());
                if(amethyst_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, amethyst_feature);
            }
            if (BPConfig.CONFIG.generateRuby.get()) {
                ConfiguredFeature<?,?> ruby_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.ruby_ore.getRegistryName());
                if(ruby_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ruby_feature);
            }
            if (BPConfig.CONFIG.generateSapphire.get()) {
                ConfiguredFeature<?,?> sapphire_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.sapphire_ore.getRegistryName());
                if(sapphire_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, sapphire_feature);
            }
            if (BPConfig.CONFIG.generateGreenSapphire.get()) {
                ConfiguredFeature<?,?> green_sapphire_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.green_sapphire_ore.getRegistryName());
                if(green_sapphire_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, green_sapphire_feature);
            }
            if (BPConfig.CONFIG.generateSilver.get()) {
                ConfiguredFeature<?,?> silver_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.silver_ore.getRegistryName());
                if(silver_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, silver_feature);
            }
            if (BPConfig.CONFIG.generateTeslatite.get()) {
                ConfiguredFeature<?,?> teslatite_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.teslatite_ore.getRegistryName());
                if(teslatite_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, teslatite_feature);
            }
            if (BPConfig.CONFIG.generateZinc.get()) {
                ConfiguredFeature<?,?> zinc_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.zinc_ore.getRegistryName());
                if(zinc_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, zinc_feature);
            }
            if (BPConfig.CONFIG.generateCopper.get()) {
                ConfiguredFeature<?,?> copper_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.copper_ore.getRegistryName());
                if(copper_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, copper_feature);
            }
            if (BPConfig.CONFIG.generateTungsten.get()) {
                ConfiguredFeature<?,?> tungsten_feature = WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(BPBlocks.tungsten_ore.getRegistryName());
                if(tungsten_feature != null)
                    generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, tungsten_feature);
            }
        }
    }
}
