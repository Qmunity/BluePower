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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BPWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, Refs.MODID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENTS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Refs.MODID);
    //VOLCANO
    public static DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> VOLCANO = FEATURES.register("volcano", () -> new WorldGenVolcano(NoneFeatureConfiguration.CODEC));
    public static Supplier<PlacementModifierType<?>> VOLCANO_PLACEMENT = PLACEMENTS.register("volcano",  () -> (PlacementModifierType<PlacementModifier>) () -> PlacementVolcano.CODEC);



}
