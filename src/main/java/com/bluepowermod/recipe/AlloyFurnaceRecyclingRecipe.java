package com.bluepowermod.recipe;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.init.BPRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MoreThanHidden
 */
public class AlloyFurnaceRecyclingRecipe implements IAlloyFurnaceRecipe {

    private final ResourceLocation id;

    public AlloyFurnaceRecyclingRecipe(ResourceLocation idIn) {
        this.id = idIn;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getId() {
        return this.id;
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
    public ItemStack assemble(WorldlyContainer inv, RegistryAccess registryAccess) {
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
    public boolean useItems(NonNullList<ItemStack> itemStacks, RecipeManager manager) {
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
    public ItemStack assemble(NonNullList<ItemStack> itemStacks, RecipeManager manager) {
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
    public NonNullList<Ingredient> getRequiredItems() {
        return null;
    }

    @Override
    public NonNullList<Integer> getRequiredCount() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<AlloyFurnaceRecyclingRecipe> {
        @Override
        public AlloyFurnaceRecyclingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new AlloyFurnaceRecyclingRecipe(recipeId);
        }

        @Override
        public AlloyFurnaceRecyclingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return new AlloyFurnaceRecyclingRecipe(recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlloyFurnaceRecyclingRecipe recipe) {
        }
    }
}
