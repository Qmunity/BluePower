package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import static com.bluepowermod.world.BPWorldGen.addFeatureToBiome;

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

    public static void setupOres(){
        if (BPConfig.CONFIG.generateAmethyst.get()) {
            addOreToGenerate(BPBlocks.amethyst_ore);
        }
        if (BPConfig.CONFIG.generateRuby.get()) {
            addOreToGenerate(BPBlocks.ruby_ore);
        }
        if (BPConfig.CONFIG.generateSapphire.get()) {
            addOreToGenerate(BPBlocks.sapphire_ore);
        }
        if (BPConfig.CONFIG.generateSilver.get()) {
            addOreToGenerate(BPBlocks.silver_ore);
        }
        if (BPConfig.CONFIG.generateTeslatite.get()) {
            addOreToGenerate(BPBlocks.teslatite_ore);
        }
        if (BPConfig.CONFIG.generateZinc.get()) {
            addOreToGenerate(BPBlocks.zinc_ore);
        }
        if (BPConfig.CONFIG.generateCopper.get()) {
            addOreToGenerate(BPBlocks.copper_ore);
        }
        if (BPConfig.CONFIG.generateTungsten.get()) {
            addOreToGenerate(BPBlocks.tungsten_ore);
        }
    }

    private static void registerConfiguredOre(int veinCount, int veinSize, int minY, int maxY, Block ore){
        if(ore.getRegistryName() != null)
         Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getRegistryName(), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ore.getDefaultState(), veinSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minY, minY, maxY))).square().func_242731_b(veinCount));
    }

    private static void addOreToGenerate(Block ore){
        for(Biome biome : ForgeRegistries.BIOMES) {
            if(!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
                addFeatureToBiome(biome, GenerationStage.Decoration.UNDERGROUND_ORES, WorldGenRegistries.CONFIGURED_FEATURE.getOrDefault(ore.getRegistryName()));
            }
        }
    }

}
