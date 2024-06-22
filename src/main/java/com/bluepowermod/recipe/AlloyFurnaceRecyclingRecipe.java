package com.bluepowermod.recipe;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.init.BPRecipeTypes;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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
    public boolean matches(CraftingInput inv, Level level) {
        //Build the blacklist based on config
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if(inv.items().stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))){
            return false;
        }

        return inv.items().stream().anyMatch(itemStack -> AlloyFurnaceRegistry.getInstance().recyclingRecipes.containsKey(itemStack.getItem()));
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
    public boolean useItems(CraftingInput itemStacks, HolderLookup.Provider provider) {
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //Check if item is in blacklist
        if(itemStacks.items().stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))){
            return false;
        }

        //Return true if an Furnace Item can be recycled
        for (ItemStack itemStack : itemStacks.items()) {
            if (AlloyFurnaceRegistry.getInstance().recyclingRecipes.containsKey(itemStack.getItem())) {
                itemStack.setCount(itemStack.getCount()-1);
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput itemStacks, HolderLookup.Provider provider) {
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //Check if item is in blacklist
        if (itemStacks.items().stream().anyMatch(itemStack -> blacklist.contains(itemStack.getItem()))) {
            return ItemStack.EMPTY;
        }

        //Return the output stack if possible.
        for (ItemStack itemStack : itemStacks.items()) {
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
                    Codec.STRING.fieldOf("type").forGetter((type) -> "bluepower:alloy_recycling")
            ).apply(instance, (type) -> new AlloyFurnaceRecyclingRecipe()));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecyclingRecipe> streamCodec() {
            return new StreamCodec<RegistryFriendlyByteBuf, AlloyFurnaceRecyclingRecipe>() {
                @Override
                public AlloyFurnaceRecyclingRecipe decode(RegistryFriendlyByteBuf byteBuf) {
                    return new AlloyFurnaceRecyclingRecipe();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf byteBuf, AlloyFurnaceRecyclingRecipe alloyFurnaceRecyclingRecipe) {

                }
            };
        }
    }
}
