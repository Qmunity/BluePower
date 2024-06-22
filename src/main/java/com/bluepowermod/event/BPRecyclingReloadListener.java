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
import com.bluepowermod.world.WorldGenVolcano;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BPRecyclingReloadListener extends SimplePreparableReloadListener<Void> {
    private final ReloadableServerResources serverResources;

    public BPRecyclingReloadListener(final ReloadableServerResources serverResources) {
        this.serverResources = serverResources;
    }

    @Override
    protected void apply(Void pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        onResourceManagerReload(serverResources.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING));
        WorldGenVolcano.updateAlterBlocks();
    }

    /**
     * Generates the Dynamic Recycling recipes on a reload.
     */
    public static void onResourceManagerReload(List<RecipeHolder<CraftingRecipe>> recipeList) {
        //Make sure this is running on the logical server
        if(Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER) {
            return;
        }

        AlloyFurnaceRegistry.getInstance().blacklist.clear();
        String[] blacklistStr = BPConfig.CONFIG.alloyFurnaceBlacklist.get().split(",");
        for (String configString : blacklistStr) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(configString));
            if (item != Items.AIR) {
                AlloyFurnaceRegistry.getInstance().blacklist.add(item);
            }
        }

        AlloyFurnaceRegistry.getInstance().recyclingRecipes.clear();
        List<ItemStack> recyclingItems = AlloyFurnaceRegistry.getInstance().recyclingItems;
        for (ItemStack outputItem : recyclingItems) {

            //Build the blacklist based on config
            Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

            for (RecipeHolder<CraftingRecipe> recipe : recipeList) {
                //Take into account other mods with Dynamic Recipes
                NonNullList<Ingredient> ingredients = null;
                try{ingredients = recipe.value().getIngredients();}catch(IllegalStateException ignored){}

                //If Recipe Contains a Recyclable Item check the recipe
                if (ingredients != null && !recipe.value().isSpecial()) {
                    int recyclingAmount = 0;
                    ItemStack currentlyRecycledInto = ItemStack.EMPTY;

                    try {
                        if (!ingredients.isEmpty()) {
                            for (Ingredient input : ingredients) {
                                if (!input.isEmpty()) {
                                    if (input.test(outputItem)) {
                                        ItemStack moltenDownItem = AlloyFurnaceRegistry.getInstance().getRecyclingStack(outputItem);
                                        if (currentlyRecycledInto.isEmpty()
                                                || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                            currentlyRecycledInto = moltenDownItem;
                                            recyclingAmount += moltenDownItem.getCount();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Throwable e) {
                        BluePower.log.error("Error when generating an Alloy Furnace recipe for item " + outputItem.getDisplayName().getString()
                                + ", recipe output: " + recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getDisplayName().getString());
                        e.printStackTrace();
                    }

                    if (recyclingAmount > 0 && recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getCount() > 0) {
                        //Try to avoid Duping
                        if (!blacklist.contains(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getItem()) && recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getCount() > recyclingAmount) {
                            blacklist.add(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getItem());
                        }

                        //Skip item if it is on the blacklist
                        if (blacklist.contains(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getItem())) {
                            continue;
                        }

                        //Divide by the Recipe Output
                        ItemStack output = new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount / recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getCount()));
                        AlloyFurnaceRegistry.getInstance().recyclingRecipes.put(recipe.value().getResultItem(ServerLifecycleHooks.getCurrentServer().registryAccess()).getItem(), output);
                    }
                }
            }
        }
    }

    @Override
    protected Void prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        return null;
    }
}
