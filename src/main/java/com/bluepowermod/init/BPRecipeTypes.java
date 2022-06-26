package com.bluepowermod.init;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.reference.Refs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author MoreThanHidden
 */
@Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Refs.MODID);
    public static final RegistryObject<RecipeType<IAlloyFurnaceRecipe>> ALLOY_SMELTING = RECIPE_TYPE.register("alloy_smelting", () -> RecipeType.simple(new ResourceLocation("bluepower:alloy_smelting")));

}
