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
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;


public class BPWorldGen {

    //VOLCANO
    public static Feature<NoFeatureConfig> VOLCANO = new WorldGenVolcano(NoFeatureConfig.CODEC);
    private static PlacementVolcano VOLCANO_PLACEMENT = new PlacementVolcano(NoPlacementConfig.CODEC);
    private static ConfiguredFeature<?, ?> VOLCANO_FEATURE;
    //MARBLE
    private static ConfiguredFeature<?, ?> MARBLE_FEATURE;

    public static void init() {
        ForgeRegistries.FEATURES.register(VOLCANO.setRegistryName("bluepower:volcano"));
        ForgeRegistries.DECORATORS.register(VOLCANO_PLACEMENT.setRegistryName("bluepower:volcano"));
        VOLCANO_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:volcano", VOLCANO.configured(IFeatureConfig.NONE).decorated(VOLCANO_PLACEMENT.configured(IPlacementConfig.NONE)));
        MARBLE_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "bluepower:marble", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BPBlocks.marble.defaultBlockState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(0, 0, 90)).squared().count(1)));
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        //Volcano
        if(BPConfig.CONFIG.volcanoBiomeCategoryWhitelist.get().contains(event.getCategory().getName())) {
            if(BPConfig.CONFIG.generateVolcano.get()) {
                generation.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, VOLCANO_FEATURE);
            }
        }
        //Marble
        if(BPConfig.CONFIG.generateMarble.get() && !event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND)) {
            generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, MARBLE_FEATURE);
        }
    }

}
