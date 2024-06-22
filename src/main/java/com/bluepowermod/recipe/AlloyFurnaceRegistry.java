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
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

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
        private final NonNullList<SizedIngredient> requiredItems;
        private final String group;

        public StandardAlloyFurnaceRecipe(String group, ItemStack craftingResult, NonNullList<SizedIngredient> requiredItems) {

            if (craftingResult == null || craftingResult.isEmpty())
                throw new IllegalArgumentException("Alloy Furnace crafting result can't be null!");
            if (requiredItems.size() > 9)
                throw new IllegalArgumentException("There can't be more than 9 crafting ingredients for the Alloy Furnace!");
            for (SizedIngredient requiredItem : requiredItems) {
                if (requiredItem.ingredient().isEmpty())
                    throw new NullPointerException("An Alloy Furnace crafting ingredient can't be null!");
            }

            this.craftingResult = craftingResult;
            this.requiredItems = requiredItems;
            this.group = group;
        }

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return width <= 3 && height <= 3;
        }

        @Override
        public ItemStack getResultItem(HolderLookup.Provider provider) {
            return craftingResult;
        }

        public ItemStack getCraftingResult() {
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
        public boolean matches(CraftingInput input, Level level) {
            for (SizedIngredient requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.count();
                for (ItemStack inputStack : input.items()) {
                    if (requiredItem.ingredient().test(inputStack)) {
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
        public boolean useItems(CraftingInput input, HolderLookup.Provider provider) {
            for (SizedIngredient requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.count();
                for (int i = 0; i < input.size(); i++) {
                    ItemStack inputStack = input.items().get(i);
                    if (requiredItem.ingredient().test(inputStack)) {
                        int itemsSubstracted = Math.min(inputStack.getCount(), itemsNeeded);
                        inputStack.setCount(inputStack.getCount() - itemsSubstracted);
                        if (inputStack.getCount() <= 0)
                            input.items().set(i, ItemStack.EMPTY);
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
        public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
            return craftingResult;
        }

        /**
         * getters for JEI plugin
         * @return
         */
        public NonNullList<SizedIngredient> getRequiredItems() {
            return requiredItems;
        }

    }

    public static class Serializer implements RecipeSerializer<IAlloyFurnaceRecipe> {

        private record RawData(String group, List<SizedIngredient> requiredItems, ItemStack craftingResult) {

            public static final MapCodec<RawData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(RawData::group),
                    ExtraCodecs.nonEmptyList(Codec.list(SizedIngredient.FLAT_CODEC)).fieldOf("ingredients").forGetter(RawData::requiredItems),
                    ItemStack.CODEC.fieldOf("result").forGetter(RawData::craftingResult)
            ).apply(instance, RawData::new));
        }

        public static final MapCodec<IAlloyFurnaceRecipe> CODEC = RawData.CODEC.flatXmap(rawData -> {
            NonNullList<SizedIngredient> ingredients = NonNullList.create();
            if (rawData.requiredItems() != null) {
                ingredients.addAll(rawData.requiredItems());
            }

            return DataResult.success(
                        new StandardAlloyFurnaceRecipe(
                                rawData.group(),
                                rawData.craftingResult(),
                                ingredients
                        )
                );
            }, (recipe) -> DataResult.success(new RawData(recipe.getGroup(), recipe.getRequiredItems(), recipe.getCraftingResult()))
        );

        public final StreamCodec<RegistryFriendlyByteBuf, IAlloyFurnaceRecipe> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, IAlloyFurnaceRecipe>() {
            @Override
            public IAlloyFurnaceRecipe decode(RegistryFriendlyByteBuf buffer) {
                String s = buffer.readUtf(32767);
                int i = buffer.readVarInt();
                NonNullList<SizedIngredient> nonnulllist = NonNullList.withSize(i, new SizedIngredient(Ingredient.EMPTY, 1));

                nonnulllist.replaceAll(ignored -> SizedIngredient.STREAM_CODEC.decode(buffer));

                ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
                return new StandardAlloyFurnaceRecipe(s, itemstack, nonnulllist);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, IAlloyFurnaceRecipe recipe) {
                if(recipe instanceof StandardAlloyFurnaceRecipe) {
                    buffer.writeUtf(((StandardAlloyFurnaceRecipe)recipe).group);
                    buffer.writeVarInt(((StandardAlloyFurnaceRecipe)recipe).requiredItems.size());

                    for (SizedIngredient ingredient :((StandardAlloyFurnaceRecipe)recipe).requiredItems ) {
                        SizedIngredient.STREAM_CODEC.encode(buffer, ingredient);
                    }

                   ItemStack.STREAM_CODEC.encode(buffer, ((StandardAlloyFurnaceRecipe)recipe).craftingResult);
                }
            }
        };

        @Override
        public MapCodec<IAlloyFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IAlloyFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}