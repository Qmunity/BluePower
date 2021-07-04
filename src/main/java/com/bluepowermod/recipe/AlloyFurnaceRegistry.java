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
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPConfig;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.util.DatapackUtils;
import com.bluepowermod.util.ItemStackUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

/**
 *
 * @author MineMaarten
 */

public class AlloyFurnaceRegistry implements IAlloyFurnaceRegistry {

    private static AlloyFurnaceRegistry INSTANCE = new AlloyFurnaceRegistry();
    public static final IRecipeType ALLOYFURNACE_RECIPE = IRecipeType.<IAlloyFurnaceRecipe>register("bluepower:alloy_smelting");

    private List<IAlloyFurnaceRecipe> alloyFurnaceRecipes = new ArrayList<IAlloyFurnaceRecipe>();
    private List<ItemStack> bufferedRecyclingItems = new ArrayList<ItemStack>();
    private Map<ItemStack, ItemStack> moltenDownMap = new HashMap<ItemStack, ItemStack>();
    private List<String> blacklist = new ArrayList<String>();

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
     * Clears existing and generates an Alloy Furnace Recipe Data Pack,
     * Mainly for Dynamically generated Recycle Recipes.
     */
    public void generateRecipeDatapack(MinecraftServer server){
        if(server != null) {
            String path = server.getWorldPath(FolderName.DATAPACK_DIR).toString();
            DatapackUtils.createBPDatapack(path);
            DatapackUtils.clearBPAlloyFurnaceDatapack(path);
            for (IAlloyFurnaceRecipe recipe : alloyFurnaceRecipes) {
                DatapackUtils.generateAlloyFurnaceRecipe(recipe, path);
            }
        }
    }

    @Override
    public void addRecipe(ResourceLocation resourceLocation, ItemStack craftingResult, Ingredient... requiredItems) {

        if (craftingResult == null || craftingResult.getItem() == Items.AIR)
            throw new NullPointerException("Can't register an Alloy Furnace recipe with a null output stack or item");
        if (craftingResult.isEmpty())
            throw new NullPointerException("Can't register an Alloy Furnace recipe with a invalid output stack or item");
        NonNullList<Ingredient> requiredStacks = NonNullList.create();
        NonNullList<Integer> requiredCount = NonNullList.create();
        for (Ingredient requiredItem : requiredItems) {
                requiredStacks.add(requiredItem);
                requiredCount.add(1);
        }
        addRecipe(new StandardAlloyFurnaceRecipe(resourceLocation, "", craftingResult, requiredStacks, requiredCount));
    }

    @Override
    public void addRecyclingRecipe(ItemStack recycledItem, String... blacklist) {

        if (recycledItem.isEmpty())
            throw new NullPointerException("Recycled item can't be null!");
        bufferedRecyclingItems.add(recycledItem);
        if (blacklist.length > 0) {
            BluePower.log.info("Added to the Alloy Furnace recycling blacklist: " + Arrays.toString(blacklist));
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

    public void generateRecyclingRecipes(RecipeManager recipeManager) {

        this.blacklist = Arrays.asList(BPConfig.CONFIG.alloyFurnaceBlacklist.get().split(","));
        List<Item> blacklist = new ArrayList<Item>();
        for (String configString : this.blacklist) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(configString));
            if (item != null) {
                blacklist.add(item);
            } else {
                BluePower.log.info("BPConfig entry \"" + configString
                        + "\" not an existing item/block name! Will not be added to the blacklist");
            }
        }

        HashMap<ItemStack, ItemStack> generated_recipes = new HashMap<>();

        for (IRecipe recipe : recipeManager.getRecipes()) {

            int recyclingAmount = 0;
            ItemStack currentlyRecycledInto = ItemStack.EMPTY;

            for (ItemStack recyclingItem : bufferedRecyclingItems) {
                try {
                    if (recipe instanceof ICraftingRecipe) {
                        if (!recipe.getIngredients().isEmpty()) {
                            for (Ingredient input : ((ICraftingRecipe)recipe).getIngredients()) {
                                if (!input.isEmpty()) {
                                    //Serialize and Deserialize the Object so the base tag isn't affected.
                                    Ingredient ingredient = Ingredient.fromJson(input.toJson());
                                    if (ingredient.test(recyclingItem)) {
                                        ItemStack moltenDownItem = getRecyclingStack(recyclingItem);
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
                    if(!blacklist.contains(recipe.getResultItem().getItem()) && recipe.getResultItem().getCount() > recyclingAmount){
                        blacklist.add(recipe.getResultItem().getItem());
                    }

                    if (blacklist.contains(recipe.getResultItem().getItem())) {
                        BluePower.log.info("Skipped adding item/block " + recipe.getResultItem().getDisplayName().getString()
                                + " to the Alloy Furnace recipes.");
                        continue;
                    }
                    //Divide by the Recipe Output
                    ItemStack resultItem = new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount / recipe.getResultItem().getCount()));
                    generated_recipes.put(recipe.getResultItem(), resultItem);
            }
        }

        for (ItemStack inputItem : generated_recipes.keySet()) {
            //Check if for null output or blacklist
            if(generated_recipes.get(inputItem).getCount() > 0 && !blacklist.contains(inputItem.getItem())) {
                //Register Recipe
                addRecipe(inputItem.getItem().getRegistryName(), generated_recipes.get(inputItem), Ingredient.of(inputItem));
            }
        }
    }

    private ItemStack getRecyclingStack(ItemStack original) {

        ItemStack moltenDownStack = moltenDownMap.get(original);
        return moltenDownStack != null ? moltenDownStack : original;
    }

    public static class StandardAlloyFurnaceRecipe implements IAlloyFurnaceRecipe {

        private final ItemStack craftingResult;
        private final NonNullList<Ingredient> requiredItems;
        private final NonNullList<Integer> requiredCount;
        private final ResourceLocation id;
        private final String group;

        private StandardAlloyFurnaceRecipe(ResourceLocation id, String group, ItemStack craftingResult, NonNullList<Ingredient> requiredItems, NonNullList<Integer> requiredCount) {

            if (craftingResult == null || craftingResult.isEmpty())
                throw new IllegalArgumentException("Alloy Furnace crafting result can't be null!");
            if (requiredItems.size() > 9)
                throw new IllegalArgumentException("There can't be more than 9 crafting ingredients for the Alloy Furnace!");
            for (Ingredient requiredItem : requiredItems) {
                if (requiredItem.isEmpty())
                    throw new NullPointerException("An Alloy Furnace crafting ingredient can't be null!");
            }

            this.craftingResult = craftingResult;
            this.requiredItems = requiredItems;
            this.requiredCount = requiredCount;
            this.id = id;
            this.group = group;
        }

        @Override
        public boolean matches(ISidedInventory inv, World worldIn){
            NonNullList<ItemStack> input = NonNullList.withSize(9, ItemStack.EMPTY);
            if(inv instanceof TileAlloyFurnace) {
                //Get Input Slots first 2 are Fuel and Output
                for (int i = 2; i < 11; i++) {
                    input.set(i - 2, inv.getItem(i));
                }
            }else{
                //Get Input Slots first slot is Output
                for (int i = 1; i < 10; i++) {
                    input.set(i - 1, inv.getItem(i));
                }
            }
            return matches(input);
        }

        @Override
        public ItemStack assemble(ISidedInventory inv) {
            NonNullList<ItemStack> input = NonNullList.withSize(9, ItemStack.EMPTY);
            if(inv instanceof TileAlloyFurnace) {
                //Get Input Slots first 2 are Fuel and Output
                for (int i = 2; i < 12; i++) {
                    input.set(i - 2, inv.getItem(i));
                }
            }else{
                //Get Input Slots first is Output
                for (int i = 1; i < 11; i++) {
                    input.set(i - 1, inv.getItem(i));
                }
            }

            return assemble(input);
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return width <= 3 && height <= 3;
        }

        @Override
        public ItemStack getResultItem() {
            return craftingResult;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public String getGroup() {
            return group;
        }

        @Override
        public ItemStack getToastSymbol() {
            return new ItemStack(BPBlocks.alloyfurnace);
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return BPRecipeSerializer.ALLOYSMELTING;
        }

        @Override
        public IRecipeType<?> getType() {
            return ALLOYFURNACE_RECIPE;
        }

        @Override
        public boolean matches(NonNullList<ItemStack> input) {

            for (int i = 0; i < requiredItems.size(); i++) {
                int itemsNeeded = requiredCount.get(i);
                for (ItemStack inputStack : input) {
                    if (requiredItems.get(i).test(inputStack)) {
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
        public boolean useItems(NonNullList<ItemStack> input) {
            for (int j = 0; j < requiredItems.size(); j++) {
                int itemsNeeded = requiredCount.get(j);
                for (int i = 0; i < input.size(); i++) {
                    ItemStack inputStack = input.get(i);
                    if (requiredItems.get(j).test(inputStack)) {
                        int itemsSubstracted = Math.min(inputStack.getCount(), itemsNeeded);
                        inputStack.setCount(inputStack.getCount() - itemsSubstracted);
                        if (inputStack.getCount() <= 0)
                            input.set(i, ItemStack.EMPTY);
                        itemsNeeded -= itemsSubstracted;
                        if (itemsNeeded <= 0)
                            break;
                    }
                }
                if (itemsNeeded > 0) {
                    BluePower.log.error("Alloy Furnace recipe using items, after using still items required?? This is a bug!");
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack assemble(NonNullList<ItemStack> input) {
            return craftingResult;
        }

        /**
         * getters for JEI plugin
         * @return
         */
        public NonNullList<Ingredient> getRequiredItems() {

            return requiredItems;
        }

        public NonNullList<Integer> getRequiredCount() {
            return requiredCount;
        }
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IAlloyFurnaceRecipe> {

        @Override
        public IAlloyFurnaceRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
            NonNullList<Integer> countlist = readCount(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for alloy furnace recipe");
            } else if (nonnulllist.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is 9");
            } else {
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                return new StandardAlloyFurnaceRecipe(recipeId, s, itemstack, nonnulllist, countlist);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        private static NonNullList<Integer> readCount(JsonArray jsonArray) {
            NonNullList<Integer> countlist = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                int count;
                if (jsonArray.get(i).isJsonObject() && ((JsonObject)jsonArray.get(i)).has("count")) {
                    count = ((JsonObject) jsonArray.get(i)).get("count").getAsInt();
                }else{
                    count = 1;
                }
                if (!ingredient.isEmpty()) {
                    countlist.add(i, count);
                }
            }
            return countlist;
        }

        @Nullable
        @Override
        public IAlloyFurnaceRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            NonNullList<Integer> countlist = NonNullList.withSize(i, 0);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            for(int j = 0; j < nonnulllist.size(); ++j) {
                countlist.set(j, buffer.readInt());
            }

            ItemStack itemstack = buffer.readItem();
            return new StandardAlloyFurnaceRecipe(recipeId, s, itemstack, nonnulllist, countlist);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, IAlloyFurnaceRecipe recipe) {
            if(recipe instanceof StandardAlloyFurnaceRecipe) {
                buffer.writeUtf(((StandardAlloyFurnaceRecipe)recipe).group);
                buffer.writeVarInt(((StandardAlloyFurnaceRecipe)recipe).requiredItems.size());

                for (Ingredient ingredient :((StandardAlloyFurnaceRecipe)recipe).requiredItems ) {
                    ingredient.toNetwork(buffer);
                }

                for (int i :((StandardAlloyFurnaceRecipe)recipe).requiredCount ) {
                    buffer.writeInt(i);
                }

                buffer.writeItem(((StandardAlloyFurnaceRecipe)recipe).craftingResult);
            }
        }
    }
}