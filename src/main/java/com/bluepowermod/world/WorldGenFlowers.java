package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

import static net.minecraft.world.biome.DefaultBiomeFeatures.field_226831_z_;

public class WorldGenFlowers {

    public static void setupFlowers(){
        for(Biome biome : ForgeRegistries.BIOMES) {

            int n = 0;
            if (biome == Biomes.BIRCH_FOREST)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.BIRCH_FOREST_HILLS)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.PLAINS)
                n = BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.FOREST)
                n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();
            else if (biome == Biomes.DARK_FOREST)
                n = 2 * BPConfig.CONFIG.flowerSpawnChance.get();

            BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).func_227407_a_(BPBlocks.indigo_flower.getDefaultState(), 2), new SimpleBlockPlacer())).func_227315_a_(64).func_227322_d_();
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227247_y_.func_225566_b_(featureConfig).func_227228_a_(Placement.COUNT_HEIGHTMAP_DOUBLE.func_227446_a_(new FrequencyConfig(n))));
        }
    }

}
