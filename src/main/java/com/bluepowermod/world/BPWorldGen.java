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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class BPWorldGen {
    //VOLCANO
    public static Feature<NoFeatureConfig> VOLCANO;
    private static PlacementVolcano VOLCANO_PLACEMENT;
    private static ConfiguredFeature<?, ?> VOLCANO_FEATURE;
    //MARBLE
    private static ConfiguredFeature<?, ?> MARBLE_FEATURE;

    public static void init() {
        VOLCANO = Registry.register(Registry.FEATURE, "bluepower:volcano", new WorldGenVolcano(NoFeatureConfig.field_236558_a_));
        VOLCANO_PLACEMENT = Registry.register(Registry.DECORATOR, "bluepower:volcano", new PlacementVolcano(NoPlacementConfig.field_236555_a_));
        VOLCANO_FEATURE = Registry.register(WorldGenRegistries.field_243653_e, "bluepower:volcano", VOLCANO.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(VOLCANO_PLACEMENT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        MARBLE_FEATURE = Registry.register(WorldGenRegistries.field_243653_e, "bluepower:marble", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, BPBlocks.marble.getDefaultState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)).withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(0, 0, 90)).func_242728_a().func_242731_b(1)));
    }

    public static void setupGeneralWorldGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
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
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = new ArrayList<>(biome.func_242440_e().func_242498_c());
        while (biomeFeatures.size() <= decoration.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(biomeFeatures.get(decoration.ordinal()));
        features.add(() -> configuredFeature);
        biomeFeatures.set(decoration.ordinal(), features);

        ObfuscationReflectionHelper.setPrivateValue(BiomeGenerationSettings.class, biome.func_242440_e(), biomeFeatures, "field_242484_f");
    }

}
