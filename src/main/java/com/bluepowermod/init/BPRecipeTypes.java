package com.bluepowermod.init;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author MoreThanHidden
 */
public class BPRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Refs.MODID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<IAlloyFurnaceRecipe>> ALLOY_SMELTING = RECIPE_TYPE.register("alloy_smelting", () -> RecipeType.simple(new ResourceLocation("bluepower:alloy_smelting")));

}
