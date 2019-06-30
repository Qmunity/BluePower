package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.Config;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType.NATURAL_STONE;
import static net.minecraft.world.gen.placement.Placement.COUNT_RANGE;

public class WorldGenOres {

    public static void setupOres(){
        if (Config.generateAmethyst) {
            addOreToGenerate(Config.veinCountAmethyst, Config.veinSizeAmethyst, Config.minAmethystY, Config.maxAmethystY, BPBlocks.amethyst_ore);
        }
        if (Config.generateRuby) {
            addOreToGenerate(Config.veinCountRuby, Config.veinSizeRuby, Config.minRubyY, Config.maxRubyY, BPBlocks.ruby_ore);
        }
        if (Config.generateSapphire) {
            addOreToGenerate(Config.veinCountSapphire, Config.veinSizeSapphire, Config.minSapphireY, Config.maxSapphireY, BPBlocks.sapphire_ore);
        }
        if (Config.generateSilver) {
            addOreToGenerate(Config.veinCountSilver, Config.veinSizeSilver, Config.minSilverY, Config.maxSilverY, BPBlocks.silver_ore);
        }
        if (Config.generateTeslatite) {
            addOreToGenerate(Config.veinCountTeslatite, Config.veinSizeTeslatite, Config.minTeslatiteY, Config.maxTeslatiteY, BPBlocks.teslatite_ore);
        }
        if (Config.generateZinc) {
            addOreToGenerate(Config.veinCountZinc, Config.veinSizeZinc, Config.minZincY, Config.maxZincY, BPBlocks.zinc_ore);
        }
        if (Config.generateCopper) {
            addOreToGenerate(Config.veinCountCopper, Config.veinSizeCopper, Config.minCopperY, Config.maxCopperY, BPBlocks.copper_ore);
        }
        if (Config.generateTungsten) {
            addOreToGenerate(Config.veinCountTungsten, Config.veinSizeTungsten, Config.minTungstenY, Config.maxTungstenY, BPBlocks.tungsten_ore);
        }

    }

    private static void addOreToGenerate(int veinCount, int veinSize, int minY, int maxY, Block ore){
        for(Biome biome : ForgeRegistries.BIOMES) {
            if(!biome.getCategory().equals(Biome.Category.NETHER) || !biome.getCategory().equals(Biome.Category.THEEND)) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE,
                        new OreFeatureConfig(NATURAL_STONE, ore.getDefaultState(), veinSize),
                        COUNT_RANGE, new CountRangeConfig(veinCount, minY, minY, maxY)));
            }
        }
    }

}
