/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.world;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.google.common.collect.Lists;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class BPWorldGen {
    //VOLCANO
    public static Feature<NoFeatureConfig> VOLCANO = new WorldGenVolcano(NoFeatureConfig.field_236558_a_);
    private static PlacementVolcano VOLCANO_PLACEMENT = new PlacementVolcano(NoPlacementConfig.CODEC);
    private static ConfiguredFeature<?, ?> VOLCANO_FEATURE;
    //MARBLE
    private static ConfiguredFeature<?, ?> MARBLE_FEATURE;

    public static void init() {
        ForgeRegistries.FEATURES.register(VOLCANO.setRegistryName("bluepower:volcano"));
        ForgeRegistries.DECORATORS.register(VOLCANO_PLACEMENT.setRegistryName("bluepower:volcano"));
        VOLCANO_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:volcano", VOLCANO.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(VOLCANO_PLACEMENT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        MARBLE_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:marble", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BPBlocks.marble.getDefaultState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, 90)).square().func_242731_b(1)));
    }

    public static void setupGeneralWorldGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (Arrays.stream(BPConfig.CONFIG.volcanoBiomeCategoryWhitelist.get().split(",")).anyMatch(s -> s.equals(biome.getCategory().getName()))) {
                if(BPConfig.CONFIG.generateVolcano.get()) {
                    addFeatureToBiome(biome, GenerationStage.Decoration.LOCAL_MODIFICATIONS, VOLCANO_FEATURE);
                }
                if(BPConfig.CONFIG.generateMarble.get()) {
                    addFeatureToBiome(biome, GenerationStage.Decoration.UNDERGROUND_ORES, MARBLE_FEATURE);
                }
            }
        }
    }

    public static void addFeatureToBiome(Biome biome, GenerationStage.Decoration decoration, ConfiguredFeature<?, ?> configuredFeature) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = new ArrayList<>(biome.getGenerationSettings().getFeatures());
        while (biomeFeatures.size() <= decoration.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(biomeFeatures.get(decoration.ordinal()));
        features.add(() -> configuredFeature);
        biomeFeatures.set(decoration.ordinal(), features);

        ObfuscationReflectionHelper.setPrivateValue(BiomeGenerationSettings.class, biome.getGenerationSettings(), biomeFeatures, "field_242484_f");
    }

}
