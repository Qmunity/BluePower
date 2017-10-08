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

package com.bluepowermod.recipe;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.init.Config;
import com.bluepowermod.util.ItemStackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

/**
 *
 * @author MineMaarten
 */

public class AlloyFurnaceRegistry implements IAlloyFurnaceRegistry {

    private static AlloyFurnaceRegistry INSTANCE = new AlloyFurnaceRegistry();

    private final List<IAlloyFurnaceRecipe> alloyFurnaceRecipes = new ArrayList<IAlloyFurnaceRecipe>();
    private final List<ItemStack> bufferedRecyclingItems = new ArrayList<ItemStack>();
    private final Map<ItemStack, ItemStack> moltenDownMap = new HashMap<ItemStack, ItemStack>();
    private final List<String> blacklist = new ArrayList<String>();

    private AlloyFurnaceRegistry() {

    }

    public static AlloyFurnaceRegistry getInstance() {

        return INSTANCE;
    }

    @Override
    public void addRecipe(IAlloyFurnaceRecipe recipe) {

        alloyFurnaceRecipes.add(recipe);
    }

    /**
     * getter for NEI plugin
     *
     * @return
     */
    public List<IAlloyFurnaceRecipe> getAllRecipes() {

        return alloyFurnaceRecipes;
    }

    @Override
    public void addRecipe(ItemStack craftingResult, Object... requiredItems) {

        if (craftingResult == null || craftingResult.getItem() == null)
            throw new NullPointerException("Can't register an Alloy Furnace recipe with a null output stack or item");
        if (craftingResult.isEmpty())
            throw new NullPointerException("Can't register an Alloy Furnace recipe with a invalid output stack or item");
        NonNullList<ItemStack> requiredStacks = NonNullList.withSize(requiredItems.length, ItemStack.EMPTY);
        for (int i = 0; i < requiredStacks.size(); i++) {
            if (requiredItems[i] instanceof ItemStack) {
                requiredStacks.set(i, (ItemStack) requiredItems[i]);
            } else if (requiredItems[i] instanceof Item) {
                requiredStacks.set(i, new ItemStack((Item) requiredItems[i], 1, OreDictionary.WILDCARD_VALUE));
            } else if (requiredItems[i] instanceof Block) {
                requiredStacks.set(i, new ItemStack(Item.getItemFromBlock((Block) requiredItems[i]), 1, OreDictionary.WILDCARD_VALUE));
            } else {
                throw new IllegalArgumentException("Alloy Furnace crafting ingredients can only be ItemStack, Item or Block!");
            }
        }
        addRecipe(new StandardAlloyFurnaceRecipe(craftingResult, requiredStacks));
    }

    @Override
    public void addRecyclingRecipe(ItemStack recycledItem, String... blacklist) {

        if (recycledItem.isEmpty())
            throw new NullPointerException("Recycled item can't be null!");
        bufferedRecyclingItems.add(recycledItem);
        if (blacklist.length > 0) {
            ModContainer mc = Loader.instance().activeModContainer();
            BluePower.log.info((mc != null ? mc.getName() : "Unknown mod") + " added to the Alloy Furnace recycling blacklist: "
                    + Arrays.toString(blacklist));
            Collections.addAll(this.blacklist, blacklist);
        }
    }

    @Override
    public void addRecyclingRecipe(ItemStack recycledItem, ItemStack moltenDownItem, String... blacklist) {

        if (moltenDownItem.isEmpty())
            throw new NullPointerException("Molten down item can't be null!");
        addRecyclingRecipe(recycledItem, blacklist);
        moltenDownMap.put(recycledItem, moltenDownItem);
    }

    @SuppressWarnings("unchecked")
    public void generateRecyclingRecipes() {

        Collections.addAll(blacklist, Config.alloyFurnaceBlacklist);
        List<Item> blacklist = new ArrayList<Item>();
        for (String configString : this.blacklist) {
            Item item = Item.getByNameOrId(configString);
            if (item != null) {
                blacklist.add(item);
            } else {
                BluePower.log.info("Config entry \"" + configString
                        + "\" not an existing item/block name! Will not be added to the blacklist");
            }
        }

        List<ItemStack> registeredRecycledItems = new ArrayList<ItemStack>();
        List<ItemStack> registeredResultItems = new ArrayList<ItemStack>();

        for (IRecipe recipe : CraftingManager.REGISTRY) {
            int recyclingAmount = 0;
            ItemStack currentlyRecycledInto = ItemStack.EMPTY;
            for (ItemStack recyclingItem : bufferedRecyclingItems) {
                try {
                    if (recipe instanceof ShapedRecipes) {
                        ShapedRecipes shaped = (ShapedRecipes) recipe;
                        if (shaped.recipeItems != null) {
                            for (Ingredient input : shaped.recipeItems) {
                                if (ItemStackUtils.isItemFuzzyEqual(input, recyclingItem)) {
                                    ItemStack moltenDownItem = getRecyclingStack(recyclingItem);
                                    if (currentlyRecycledInto.isEmpty()
                                            || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                        currentlyRecycledInto = moltenDownItem;
                                        recyclingAmount += moltenDownItem.getCount();
                                    }
                                }
                            }
                        }
                    } else if (recipe instanceof ShapelessRecipes) {
                        ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
                        if (!shapeless.recipeItems.isEmpty()) {
                            for (Ingredient input : shapeless.recipeItems) {
                                if (ItemStackUtils.isItemFuzzyEqual(input, recyclingItem)) {
                                    ItemStack moltenDownItem = getRecyclingStack(recyclingItem);
                                    if (currentlyRecycledInto.isEmpty()
                                            || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                        currentlyRecycledInto = moltenDownItem;
                                        recyclingAmount += moltenDownItem.getCount();
                                    }
                                }
                            }
                        }
                    } else if (recipe instanceof ShapedOreRecipe) {
                        ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) recipe;
                            for (Ingredient input : shapedOreRecipe.getIngredients()) {
                                if (input != null) {
                                    List<ItemStack> itemList = (List<ItemStack>) input;
                                    for (ItemStack item : itemList) {
                                        if (!item.isEmpty() && ItemStackUtils.isItemFuzzyEqual(item, recyclingItem)) {
                                            ItemStack moltenDownItem = getRecyclingStack(recyclingItem);
                                            if (currentlyRecycledInto.isEmpty()
                                                    || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                                currentlyRecycledInto = moltenDownItem;
                                                recyclingAmount += moltenDownItem.getCount();
                                            }
                                            break;
                                        }
                                    }
                                }
                        }
                    } else if (recipe instanceof ShapelessOreRecipe) {
                        ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;
                        for (Ingredient input : shapeless.getIngredients()) {
                            if (input != null) {
                                List<ItemStack> itemList = (List<ItemStack>) input;
                                for (ItemStack item : itemList) {
                                    if (!item.isEmpty() && ItemStackUtils.isItemFuzzyEqual(item, recyclingItem)) {
                                        ItemStack moltenDownItem = getRecyclingStack(recyclingItem);
                                        if (currentlyRecycledInto.isEmpty()
                                                || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                            currentlyRecycledInto = moltenDownItem;
                                            recyclingAmount += moltenDownItem.getCount();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable e) {
                    BluePower.log.error("Error when generating an Alloy Furnace recipe for item " + recyclingItem.getDisplayName()
                            + ", recipe output: " + recipe.getRecipeOutput().getDisplayName());
                    e.printStackTrace();
                }
            }
            if (recyclingAmount > 0 && recipe.getRecipeOutput().getCount() > 0) {
                boolean shouldAdd = true;
                for (int i = 0; i < registeredRecycledItems.size(); i++) {
                    if (ItemStackUtils.isItemFuzzyEqual(registeredRecycledItems.get(i), recipe.getRecipeOutput())) {
                        if (registeredResultItems.get(i).getCount() < recyclingAmount) {
                            shouldAdd = false;
                            break;
                        } else {
                            registeredResultItems.remove(i);
                            registeredRecycledItems.remove(i);
                            i--;
                        }
                    }
                }

                if (shouldAdd) {
                    if (blacklist.contains(recipe.getRecipeOutput().getItem())) {
                        BluePower.log.info("Skipped adding item/block " + recipe.getRecipeOutput().getDisplayName()
                                + " to the Alloy Furnace recipes.");
                        continue;
                    }
                    ItemStack resultItem = new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount),
                            currentlyRecycledInto.getItemDamage());
                    registeredResultItems.add(resultItem);
                    registeredRecycledItems.add(recipe.getRecipeOutput());

                }
            }
        }
        for (int i = 0; i < registeredResultItems.size(); i++) {
            addRecipe(registeredResultItems.get(i), registeredRecycledItems.get(i));
        }

    }

    private ItemStack getRecyclingStack(ItemStack original) {

        ItemStack moltenDownStack = moltenDownMap.get(original);
        return !moltenDownStack.isEmpty() ? moltenDownStack : original;
    }

    public IAlloyFurnaceRecipe getMatchingRecipe(NonNullList<ItemStack> input, ItemStack outputSlot) {

        for (IAlloyFurnaceRecipe recipe : alloyFurnaceRecipes) {
            if (recipe.matches(input)) {
                if (outputSlot != null && !outputSlot.isEmpty()) {// check if we can add the crafting result to the output slot
                    ItemStack craftingResult = recipe.getCraftingResult(input);
                    if (!ItemStack.areItemStackTagsEqual(outputSlot, craftingResult) || !outputSlot.isItemEqual(craftingResult)) {
                        continue;
                    } else if (craftingResult.getCount() + outputSlot.getCount() > outputSlot.getMaxStackSize()) {
                        continue;
                    }
                }
                return recipe;
            }
        }
        return null;
    }

    public class StandardAlloyFurnaceRecipe implements IAlloyFurnaceRecipe {

        private final ItemStack craftingResult;
        private final NonNullList<ItemStack> requiredItems;

        private StandardAlloyFurnaceRecipe(ItemStack craftingResult, NonNullList<ItemStack> requiredItems) {

            if (craftingResult.isEmpty())
                throw new IllegalArgumentException("Alloy Furnace crafting result can't be null!");
            if (requiredItems.size() > 9)
                throw new IllegalArgumentException("There can't be more than 9 crafting ingredients for the Alloy Furnace!");
            for (ItemStack requiredItem : requiredItems) {
                if (requiredItem.isEmpty())
                    throw new NullPointerException("An Alloy Furnace crafting ingredient can't be null!");
            }
            for (ItemStack stack : requiredItems) {
                for (ItemStack stack2 : requiredItems) {
                    if (stack != stack2 && ItemStackUtils.isItemFuzzyEqual(stack, stack2))
                        throw new IllegalArgumentException(
                                "No equivalent Alloy Furnace crafting ingredient can be given twice! This does take OreDict + wildcard values in account.");
                }
            }

            this.craftingResult = craftingResult;
            this.requiredItems = requiredItems;
        }

        @Override
        public boolean matches(NonNullList<ItemStack> input) {

            for (ItemStack requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.getCount();
                for (ItemStack inputStack : input) {
                    if (!inputStack.isEmpty() && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)) {
                        itemsNeeded -= inputStack.getCount();
                        if (itemsNeeded <= 0)
                            break;
                    }
                }
                if (itemsNeeded > 0)
                    return false;
            }
            return true;
        }

        @Override
        public void useItems(NonNullList<ItemStack> input) {

            for (ItemStack requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.getCount();
                for (int i = 0; i < input.size(); i++) {
                    ItemStack inputStack = input.get(i);
                    if (!inputStack.isEmpty() && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)) {
                        int itemsSubstracted = Math.min(inputStack.getCount(), itemsNeeded);
                        inputStack.setCount(inputStack.getCount() - itemsSubstracted);
                        if (inputStack.getCount() <= 0)
                            input.set(i, ItemStack.EMPTY);
                        itemsNeeded -= itemsSubstracted;
                        if (itemsNeeded <= 0)
                            break;
                    }
                }
                if (itemsNeeded > 0)
                    throw new IllegalArgumentException(
                            "Alloy Furnace recipe using items, after using still items required?? This is a bug!");
            }
        }

        @Override
        public ItemStack getCraftingResult(NonNullList<ItemStack> input) {

            return craftingResult;
        }

        /**
         * getter for NEI plugin
         *
         * @return
         */
        public NonNullList<ItemStack> getRequiredItems() {

            return requiredItems;
        }

    }

}