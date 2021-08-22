package com.bluepowermod.recipe;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.util.ItemStackUtils;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MoreThanHidden
 */
public class AlloyFurnaceRecyclingRecipe implements IAlloyFurnaceRecipe {

    private final ResourceLocation id;

    public AlloyFurnaceRecyclingRecipe(ResourceLocation idIn) {
        this.id = idIn;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE;
    }

    @Override
    public boolean matches(WorldlyContainer inv, Level world) {
        //Build the blacklist based on config
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if(inv.hasAnyOf(blacklist)){
            return false;
        }

        //Filter recipes to just those containing recycling items
        Collection<Recipe<?>> recipes = world.getRecipeManager().getRecipes().stream().filter(
                recipe -> recipe.getIngredients().stream().anyMatch(
                        ingredient -> Arrays.stream(ingredient.getItems()).anyMatch(
                                item -> AlloyFurnaceRegistry.getInstance().recyclingItems.stream().anyMatch(target -> item.getItem() == target.getItem())
                        )
                )
        ).collect(Collectors.toList());

        //Return true if an Furnace Item can be recycled
        for (int i = 0; i < inv.getContainerSize(); i++) {
            for (Recipe<?> recipe : recipes){
                if(recipe.getResultItem().getItem() == inv.getItem(i).getItem()){
                    return recipe.getIngredients().stream().filter(ingredient -> AlloyFurnaceRegistry.getInstance().recyclingItems.stream().anyMatch(ingredient::test)).count() / recipe.getResultItem().getCount() >= 1;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(WorldlyContainer inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.ALLOY_RECYCLING;
    }

    @Override
    public boolean matches(NonNullList<ItemStack> input) {
        return false;
    }

    @Override
    public boolean useItems(NonNullList<ItemStack> itemStacks, RecipeManager manager) {
        //Build the blacklist based on config
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if(itemStacks.stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))){
            return false;
        }

        //Filter recipes to just those containing recycling items
        Collection<Recipe<?>> recipes = manager.getRecipes().stream().filter(
                recipe -> recipe.getIngredients().stream().anyMatch(
                        ingredient -> Arrays.stream(ingredient.getItems()).anyMatch(
                                item -> AlloyFurnaceRegistry.getInstance().recyclingItems.stream().anyMatch(target -> item.getItem() == target.getItem())
                        )
                )
        ).collect(Collectors.toList());

        //Return true if an Furnace Item can be recycled
        for (ItemStack itemStack : itemStacks) {
            for (Recipe<?> recipe : recipes) {
                if (recipe.getResultItem().getItem() == itemStack.getItem()) {
                    itemStack.setCount(itemStack.getCount()-1);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(NonNullList<ItemStack> itemStacks, RecipeManager manager) {
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if (itemStacks.stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))) {
            return ItemStack.EMPTY;
        }

        //Filter recipes to just those containing recycling items
        Collection<Recipe<?>> recipes = manager.getRecipes().stream().filter(
                recipe -> recipe.getIngredients().stream().anyMatch(
                        ingredient -> Arrays.stream(ingredient.getItems()).anyMatch(
                                item -> AlloyFurnaceRegistry.getInstance().recyclingItems.stream().anyMatch(target -> item.getItem() == target.getItem())
                        )
                )
        ).collect(Collectors.toList());

        //Return true if an Furnace Item can be recycled
        for (ItemStack itemStack : itemStacks) {
            for (Recipe<?> recipe : recipes) {
                if (recipe.getResultItem().getItem() == itemStack.getItem()) {
                    int recyclingAmount = 0;
                    ItemStack currentlyRecycledInto = ItemStack.EMPTY;

                    for (ItemStack recyclingItem : AlloyFurnaceRegistry.getInstance().recyclingItems) {
                        try {
                            if (recipe instanceof CraftingRecipe) {
                                if (!recipe.getIngredients().isEmpty()) {
                                    for (Ingredient input : recipe.getIngredients()) {
                                        if (!input.isEmpty()) {
                                            //Serialize and Deserialize the Object so the base tag isn't affected.
                                            Ingredient ingredient = Ingredient.fromJson(input.toJson());
                                            if (ingredient.test(recyclingItem)) {
                                                ItemStack moltenDownItem = AlloyFurnaceRegistry.getInstance().getRecyclingStack(recyclingItem);
                                                if (currentlyRecycledInto.isEmpty()
                                                        || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                                    currentlyRecycledInto = moltenDownItem;
                                                    recyclingAmount += moltenDownItem.getCount();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            BluePower.log.error("Error when generating an Alloy Furnace recipe for item " + recyclingItem.getDisplayName().getString()
                                    + ", recipe output: " + recipe.getResultItem().getDisplayName().getString());
                            e.printStackTrace();
                        }
                    }


                    if (recyclingAmount > 0 && recipe.getResultItem().getCount() > 0) {
                        //Try to avoid Duping
                        if (!blacklist.contains(recipe.getResultItem().getItem()) && recipe.getResultItem().getCount() > recyclingAmount) {
                            blacklist.add(recipe.getResultItem().getItem());
                        }

                        //Skip item if it is on the blacklist
                        if (blacklist.contains(recipe.getResultItem().getItem())) {
                            continue;
                        }

                        //Divide by the Recipe Output
                        return new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount / recipe.getResultItem().getCount()));

                    }
                }
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

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlloyFurnaceRecyclingRecipe> {
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
