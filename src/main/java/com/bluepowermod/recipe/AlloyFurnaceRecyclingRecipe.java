package com.bluepowermod.recipe;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPRecipeSerializer;
import com.bluepowermod.util.ItemStackUtils;
import com.google.gson.JsonObject;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
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
    public IRecipeType<?> getType() {
        return AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE;
    }

    @Override
    public boolean matches(ISidedInventory inv, World world) {
        //Build the blacklist based on config
        Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

        //item is in blacklist
        if(inv.hasAnyOf(blacklist)){
            return false;
        }

        return inv.hasAnyOf(AlloyFurnaceRegistry.getInstance().recyclingRecipes.keySet());

    }

    @Override
    public ItemStack assemble(ISidedInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BPRecipeSerializer.ALLOY_RECYCLING;
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

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloyFurnaceRecyclingRecipe> {
        @Override
        public AlloyFurnaceRecyclingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return new AlloyFurnaceRecyclingRecipe(recipeId);
        }

        @Override
        public AlloyFurnaceRecyclingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            return new AlloyFurnaceRecyclingRecipe(recipeId);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, AlloyFurnaceRecyclingRecipe recipe) {
        }
    }
}
