package com.bluepowermod.recipe;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class FurnaceRecipeGetter {
    private static FurnaceRecipeGetter instance = new FurnaceRecipeGetter();

    private static List<FurnaceRecipe> recipeList = new ArrayList<>();

    public static void initRecipes(){
        for(Item item : Item.REGISTRY) {
            NonNullList<ItemStack> items = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, items);
            for(ItemStack stack : items) {
                if (!FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty()){
                    recipeList.add(new FurnaceRecipe(stack, FurnaceRecipes.instance().getSmeltingResult(stack)));
                }
            }
        }
    }

    public static FurnaceRecipeGetter getInstance() {
        return instance;
    }

    public FurnaceRecipe getRecipe(ItemStack input, ItemStack output){
        if (!output.isEmpty() && output.equals(FurnaceRecipes.instance().getSmeltingResult(input))){
            for (FurnaceRecipe recipe : recipeList){
                if (recipe.input.equals(input)){
                    return recipe;
                }
            }
        }
        return null;
    }

    public static class FurnaceRecipe {
        ItemStack input;
        ItemStack output;
        private FurnaceRecipe(ItemStack input, ItemStack output){
            this.input = input;
            this.output = output;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }
    }
}
