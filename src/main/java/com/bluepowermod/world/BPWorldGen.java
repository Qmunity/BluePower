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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.registries.ForgeRegistries;


public class BPWorldGen {

    //VOLCANO
    public static final Feature<NoFeatureConfig> VOLCANO = new WorldGenVolcano(NoFeatureConfig::deserialize);

    public static void setupGeneralWorldGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
                biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, VOLCANO.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG).func_227228_a_(new PlacementVolcano(NoPlacementConfig::deserialize).func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.func_225566_b_(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BPBlocks.marble.getDefaultState(), BPConfig.CONFIG.veinSizeMarble.get() / 32)).func_227228_a_(Placement.COUNT_RANGE.func_227446_a_(new CountRangeConfig(1, 0, 0, 90))));
            }
        }
    }
}
