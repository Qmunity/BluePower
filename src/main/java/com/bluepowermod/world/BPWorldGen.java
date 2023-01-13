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

import com.bluepowermod.reference.Refs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;

public class BPWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Refs.MODID);
    //VOLCANO
    public static RegistryObject<Feature<NoneFeatureConfiguration>> VOLCANO = FEATURES.register("volcano", () -> new WorldGenVolcano(NoneFeatureConfiguration.CODEC));
    public static PlacementModifierType<?> VOLCANO_PLACEMENT;

    public static void registerFeatures() {
        VOLCANO_PLACEMENT = Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, "bluepower:volcano", () -> PlacementVolcano.CODEC);
    }
}
