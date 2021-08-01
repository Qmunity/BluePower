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
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.CountDecorator;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;


import net.minecraft.world.level.levelgen.feature.Feature;

public class BPWorldGen {

    //VOLCANO
    public static Feature<NoneFeatureConfiguration> VOLCANO = new WorldGenVolcano(NoneFeatureConfiguration.CODEC);
    private static PlacementVolcano VOLCANO_PLACEMENT = new PlacementVolcano(NoneDecoratorConfiguration.CODEC);
    private static ConfiguredFeature<?, ?> VOLCANO_FEATURE;
    //MARBLE
    private static ConfiguredFeature<?, ?> MARBLE_FEATURE;

    public static void init() {
        ForgeRegistries.FEATURES.register(VOLCANO.setRegistryName("bluepower:volcano"));
        ForgeRegistries.DECORATORS.register(VOLCANO_PLACEMENT.setRegistryName("bluepower:volcano"));
        VOLCANO_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:volcano", VOLCANO.configured(FeatureConfiguration.NONE).decorated(VOLCANO_PLACEMENT.configured(DecoratorConfiguration.NONE)));
        MARBLE_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, "bluepower:marble", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, BPBlocks.marble.defaultBlockState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)).decorated(CountDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.absolute(0) , VerticalAnchor.absolute(90)))).squared().count(1)));
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event){
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        //Volcano
        if(BPConfig.CONFIG.volcanoBiomeCategoryWhitelist.get().contains(event.getCategory().getName())) {
            if(BPConfig.CONFIG.generateVolcano.get()) {
                generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, VOLCANO_FEATURE);
            }
        }
        //Marble
        if(BPConfig.CONFIG.generateMarble.get() && !event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MARBLE_FEATURE);
        }
    }

}
