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
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.init.BPRecipeTypes;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author MoreThanHidden
 */
public class AlloyFurnaceRegistry {

    private static AlloyFurnaceRegistry INSTANCE = new AlloyFurnaceRegistry();

    private Map<ItemStack, ItemStack> moltenDownMap = new HashMap<ItemStack, ItemStack>();
    public List<Item> blacklist = new ArrayList<>();
    public List<ItemStack> recyclingItems = new ArrayList<>();
    //Input Item to Output Stack
    public HashMap<Item, ItemStack> recyclingRecipes = new HashMap<>();

    private AlloyFurnaceRegistry() {

    }
    public static AlloyFurnaceRegistry getInstance() {

        return INSTANCE;
    }

    public ItemStack getRecyclingStack(ItemStack original) {

        ItemStack moltenDownStack = moltenDownMap.get(original);
        return moltenDownStack != null ? moltenDownStack : original;
    }

    public static class StandardAlloyFurnaceRecipe implements IAlloyFurnaceRecipe {

        private final ItemStack craftingResult;
        private final NonNullList<Ingredient> requiredItems;
        private final NonNullList<Integer> requiredCount;
        private final String group;

        public StandardAlloyFurnaceRecipe(String group, ItemStack craftingResult, NonNullList<Ingredient> requiredItems, NonNullList<Integer> requiredCount) {

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
            this.group = group;
        }

        @Override
        public boolean matches(WorldlyContainer inv, Level worldIn){
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
        public ItemStack assemble(WorldlyContainer inv, RegistryAccess registryAccess) {
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

            return assemble(input, null);
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return width <= 3 && height <= 3;
        }

        @Override
        public ItemStack getResultItem(RegistryAccess registryAccess) {
            return craftingResult;
        }

        @Override
        public String getGroup() {
            return group;
        }

        @Override
        public ItemStack getToastSymbol() {
            return new ItemStack(BPBlocks.alloyfurnace.get());
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return BPRecipeSerializer.ALLOYSMELTING.get();
        }

        @Override
        public RecipeType<?> getType() {
            return BPRecipeTypes.ALLOY_SMELTING.get();
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
        public boolean useItems(NonNullList<ItemStack> input, RecipeManager recipeManager) {
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
        public ItemStack assemble(NonNullList<ItemStack> input, RecipeManager recipeManager) {
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

    public static class Serializer implements RecipeSerializer<IAlloyFurnaceRecipe> {

        private record RawData(String group, List<Ingredient> requiredItems, List<Integer> requiredCount, ItemStack craftingResult) {

                public static final Codec<RawData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(RawData::group),
                        ExtraCodecs.nonEmptyList(Codec.list(Ingredient.CODEC)).fieldOf("ingredients").forGetter(RawData::requiredItems),
                        ExtraCodecs.nonEmptyList(Codec.list(Codec.INT)).fieldOf("count").forGetter(RawData::requiredCount),
                        ItemStack.CODEC.fieldOf("result").forGetter(RawData::craftingResult)
                ).apply(instance, RawData::new));
        }

        public static final Codec<IAlloyFurnaceRecipe> CODEC = RawData.CODEC.flatXmap(rawData -> {
            return DataResult.success(new StandardAlloyFurnaceRecipe(rawData.group(), rawData.craftingResult(), (NonNullList<Ingredient>)rawData.requiredItems(), (NonNullList<Integer>)rawData.requiredCount()));
        }, (recipe) -> {
            if (recipe instanceof StandardAlloyFurnaceRecipe) {
                StandardAlloyFurnaceRecipe standardRecipe = (StandardAlloyFurnaceRecipe) recipe;
                return DataResult.success(new RawData(standardRecipe.getGroup(), standardRecipe.getRequiredItems(), standardRecipe.getRequiredCount(), standardRecipe.getResultItem(null)));
            } else {
                return DataResult.error(() -> "Cannot serialize IAlloyFurnaceRecipe that is not a StandardAlloyFurnaceRecipe");
            }
        });

        @Override
        public Codec<IAlloyFurnaceRecipe> codec() {
            return CODEC;
        }

        @Nullable
        @Override
        public IAlloyFurnaceRecipe fromNetwork(FriendlyByteBuf buffer) {
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
            return new StandardAlloyFurnaceRecipe(s, itemstack, nonnulllist, countlist);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, IAlloyFurnaceRecipe recipe) {
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