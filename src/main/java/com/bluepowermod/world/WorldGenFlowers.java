package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.reference.Refs;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;

import java.util.Map;

import static com.bluepowermod.world.BPWorldGen.addFeatureToBiome;

public class WorldGenFlowers {

    public static void initFlowers(){
        for(Map.Entry<RegistryKey<Biome>, Biome> biome : WorldGenRegistries.field_243657_i.getEntries()) {
            int n = getConfigAmount(biome.getKey());
            if(n > 0) {
                BlockClusterFeatureConfig featureConfig = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(BPBlocks.indigo_flower.getDefaultState(), 2), new SimpleBlockPlacer())).tries(64).build();
                Registry.register(WorldGenRegistries.field_243653_e, "bluepower:" + Refs.INDIGOFLOWER_NAME + n, Feature.FLOWER.withConfiguration(featureConfig).withPlacement(Features.Placements.field_244000_k).withPlacement(Features.Placements.field_244001_l).func_242729_a(n));
            }
        }
    }
    public static void setupFlowers() {
        for(Map.Entry<RegistryKey<Biome>, Biome> biome : WorldGenRegistries.field_243657_i.getEntries()) {
            int n = getConfigAmount(biome.getKey());
            if(n > 0) {
                addFeatureToBiome(biome.getValue(), GenerationStage.Decoration.VEGETAL_DECORATION, WorldGenRegistries.field_243653_e.getOrDefault(new ResourceLocation("bluepower:" + Refs.INDIGOFLOWER_NAME + n)));
            }
        }
    }

    static int getConfigAmount(RegistryKey<Biome> biome){
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
        return n;
    }

}
