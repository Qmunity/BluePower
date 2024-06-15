package com.bluepowermod.recipe;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.init.BPRecipeTypes;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MoreThanHidden
 */
public class AlloyFurnaceRecyclingRecipe implements IAlloyFurnaceRecipe {

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeType<?> getType() {
        return BPRecipeTypes.ALLOY_SMELTING.get();
    }

    @Override
    public boolean matches(WorldlyContainer inv, Level world) {
        //Build the blacklist based on config
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if(inv.hasAnyOf(blacklist)){
            return false;
        }

        return inv.hasAnyOf(AlloyFurnaceRegistry.getInstance().recyclingRecipes.keySet());
    }

    @Override
    public ItemStack assemble(WorldlyContainer worldlyContainer, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.ALLOY_RECYCLING.get();
    }

    @Override
    public boolean matches(NonNullList<ItemStack> input) {
        return false;
    }

    @Override
    public boolean useItems(NonNullList<ItemStack> itemStacks, HolderLookup.Provider provider) {
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //Check if item is in blacklist
        if(itemStacks.stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))){
            return false;
        }

        //Return true if an Furnace Item can be recycled
        for (ItemStack itemStack : itemStacks) {
            if (AlloyFurnaceRegistry.getInstance().recyclingRecipes.containsKey(itemStack.getItem())) {
                itemStack.setCount(itemStack.getCount()-1);
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(NonNullList<ItemStack> itemStacks, HolderLookup.Provider provider) {
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //Check if item is in blacklist
        if (itemStacks.stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))) {
            return ItemStack.EMPTY;
        }

        //Return the output stack if possible.
        for (ItemStack itemStack : itemStacks) {
            if (AlloyFurnaceRegistry.getInstance().recyclingRecipes.containsKey(itemStack.getItem())) {
                return AlloyFurnaceRegistry.getInstance().recyclingRecipes.get(itemStack.getItem());
            }
        }
        return ItemStack.EMPTY;
      
    }

    @Override
    public NonNullList<SizedIngredient> getRequiredItems() {
        return null;
    }

    @Override
    public ItemStack getCraftingResult() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<AlloyFurnaceRecyclingRecipe> {

        @Override
        public MapCodec<AlloyFurnaceRecyclingRecipe> codec() {
            return RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    Codec.list(Ingredient.CODEC).fieldOf("ingredients").forGetter(Recipe::getIngredients)
            ).apply(instance, (p1) -> new AlloyFurnaceRecyclingRecipe()));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecyclingRecipe> streamCodec() {
            return new StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecyclingRecipe>() {
                @Override
                public AlloyFurnaceRecyclingRecipe decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                    return new AlloyFurnaceRecyclingRecipe();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf o, AlloyFurnaceRecyclingRecipe alloyFurnaceRecyclingRecipe) {

                }
            };
        }
    }
}
