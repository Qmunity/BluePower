package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE;
import static net.minecraft.world.gen.placement.Placement.COUNT_RANGE;

public class WorldGenOres {

    public static void setupOres(){
        if (BPConfig.CONFIG.generateAmethyst.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountAmethyst.get(), BPConfig.CONFIG.veinSizeAmethyst.get(), BPConfig.CONFIG.minAmethystY.get(), BPConfig.CONFIG.maxAmethystY.get(), BPBlocks.amethyst_ore);
        }
        if (BPConfig.CONFIG.generateRuby.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountRuby.get(), BPConfig.CONFIG.veinSizeRuby.get(), BPConfig.CONFIG.minRubyY.get(), BPConfig.CONFIG.maxRubyY.get(), BPBlocks.ruby_ore);
        }
        if (BPConfig.CONFIG.generateSapphire.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountSapphire.get(), BPConfig.CONFIG.veinSizeSapphire.get(), BPConfig.CONFIG.minSapphireY.get(), BPConfig.CONFIG.maxSapphireY.get(), BPBlocks.sapphire_ore);
        }
        if (BPConfig.CONFIG.generateSilver.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountSilver.get(), BPConfig.CONFIG.veinSizeSilver.get(), BPConfig.CONFIG.minSilverY.get(), BPConfig.CONFIG.maxSilverY.get(), BPBlocks.silver_ore);
        }
        if (BPConfig.CONFIG.generateTeslatite.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountTeslatite.get(), BPConfig.CONFIG.veinSizeTeslatite.get(), BPConfig.CONFIG.minTeslatiteY.get(), BPConfig.CONFIG.maxTeslatiteY.get(), BPBlocks.teslatite_ore);
        }
        if (BPConfig.CONFIG.generateZinc.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountZinc.get(), BPConfig.CONFIG.veinSizeZinc.get(), BPConfig.CONFIG.minZincY.get(), BPConfig.CONFIG.maxZincY.get(), BPBlocks.zinc_ore);
        }
        if (BPConfig.CONFIG.generateCopper.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountCopper.get(), BPConfig.CONFIG.veinSizeCopper.get(), BPConfig.CONFIG.minCopperY.get(), BPConfig.CONFIG.maxCopperY.get(), BPBlocks.copper_ore);
        }
        if (BPConfig.CONFIG.generateTungsten.get()) {
            addOreToGenerate(BPConfig.CONFIG.veinCountTungsten.get(), BPConfig.CONFIG.veinSizeTungsten.get(), BPConfig.CONFIG.minTungstenY.get(), BPConfig.CONFIG.maxTungstenY.get(), BPBlocks.tungsten_ore);
        }

    }

    private static void addOreToGenerate(int veinCount, int veinSize, int minY, int maxY, Block ore){
        for(Biome biome : ForgeRegistries.BIOMES) {
            if(!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(NATURAL_STONE, ore.getDefaultState(), veinSize)).func_227228_a_(COUNT_RANGE.func_227446_a_(new CountRangeConfig(veinCount, minY, minY, maxY))));
            }
        }
    }

}
