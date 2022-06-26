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
import com.bluepowermod.reference.Refs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BPWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Refs.MODID);
    //VOLCANO
    public static RegistryObject<Feature<NoneFeatureConfiguration>> VOLCANO = FEATURES.register("volcano", () -> new WorldGenVolcano(NoneFeatureConfiguration.CODEC));
    public static PlacementModifierType<?> VOLCANO_PLACEMENT;
    public static Holder<PlacedFeature> VOLCANO_FEATURE;
    //MARBLE
    private static Holder<PlacedFeature> MARBLE_FEATURE;

    public static void registerFeatures() {
        VOLCANO_PLACEMENT = Registry.register(Registry.PLACEMENT_MODIFIERS, "bluepower:volcano", () -> PlacementVolcano.CODEC);
        VOLCANO_FEATURE = PlacementUtils.register("bluepower:volcano",
                FeatureUtils.register( "bluepower:volcano", VOLCANO.get(), FeatureConfiguration.NONE), PlacementVolcano.instance());
        MARBLE_FEATURE = PlacementUtils.register("bluepower:marble",
                FeatureUtils.register("bluepower:marble", Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, BPBlocks.marble.get().defaultBlockState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)),
                    rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier modifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(i), modifier);
    }

    @SubscribeEvent
    public void onBiomeLoad(){
        //BiomeGenerationSettingsBuilder generation = event.getGeneration();
        //Volcano
        //if(BPConfig.CONFIG.volcanoBiomeCategoryWhitelist.get().contains(event.getCategory().getName())) {
            //if(BPConfig.CONFIG.generateVolcano.get()) {
                //generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, VOLCANO_FEATURE);
            //}
        // }
        //Marble
        //if(BPConfig.CONFIG.generateMarble.get() && !event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            //generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MARBLE_FEATURE);
        //}
    }

}
