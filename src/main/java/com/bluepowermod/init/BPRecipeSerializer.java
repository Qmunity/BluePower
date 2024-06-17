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

package com.bluepowermod.init;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.recipe.AlloyFurnaceRecyclingRecipe;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.recipe.MicroblockRecipe;
import com.bluepowermod.reference.Refs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author MoreThanHidden
 */
public class BPRecipeSerializer {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Refs.MODID);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<IAlloyFurnaceRecipe>> ALLOYSMELTING = RECIPE_SERIALIZERS.register("alloy_smelting", AlloyFurnaceRegistry.Serializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AlloyFurnaceRecyclingRecipe>> ALLOY_RECYCLING = RECIPE_SERIALIZERS.register("alloy_recycling", AlloyFurnaceRecyclingRecipe.Serializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MicroblockRecipe>> MICROBLOCK = RECIPE_SERIALIZERS.register("micro_block", MicroblockRecipe.Serializer::new);

}
