/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.event;

import com.bluepowermod.BluePower;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.util.ItemStackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BPRecyclingReloadListener implements IResourceManagerReloadListener {
    private final DataPackRegistries registries;
    public static MinecraftServer server;

    public BPRecyclingReloadListener(DataPackRegistries registries){
        this.registries = registries;
    }

    /**
     * Generates the Dynamic Recycling recipes on a reload.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        onResourceManagerReload(registries.getRecipeManager());
    }

    public static void onResourceManagerReload(RecipeManager recipeManager){
        AlloyFurnaceRegistry.getInstance().blacklist.clear();
        List<String> blacklistStr = Arrays.asList(BPConfig.CONFIG.alloyFurnaceBlacklist.get().split(","));
        for (String configString : blacklistStr) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(configString));
            if (item != null) {
               AlloyFurnaceRegistry.getInstance().blacklist.add(item);
            }
        }

        AlloyFurnaceRegistry.getInstance().recyclingRecipes.clear();

        for (ItemStack outputItem : AlloyFurnaceRegistry.getInstance().recyclingItems){

            //Build the blacklist based on config
            Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

            for (IRecipe<?> recipe : recipeManager.getAllRecipesFor(IRecipeType.CRAFTING)) {

                //Take into account other mods with Dynamic Recipes
                NonNullList<Ingredient> ingredients = null;
                try{ingredients = recipe.getIngredients();}catch(IllegalStateException | NullPointerException ignored){}

                //If Recipe Contains a Recyclable Item check the recipe
                if (ingredients != null && ingredients.stream().anyMatch(ingredient -> ingredient.test(outputItem))){
                    int recyclingAmount = 0;
                    ItemStack currentlyRecycledInto = ItemStack.EMPTY;

                    for (ItemStack recyclingItem : AlloyFurnaceRegistry.getInstance().recyclingItems) {
                        try {
                            if (recipe instanceof ICraftingRecipe) {
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
                        ItemStack output = new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount / recipe.getResultItem().getCount()));
                        AlloyFurnaceRegistry.getInstance().recyclingRecipes.put(recipe.getResultItem().getItem(), output);
                    }
                }
            }
        }
    }
}
