/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.recipe.AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe;
import com.bluepowermod.reference.Refs;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
  @author MoreThanHidden
*/


public class AlloyFurnaceHandler implements IRecipeCategory<AlloyFurnaceWrapper> {

    private final IDrawable background;

    public AlloyFurnaceHandler(IGuiHelper helper) {
      background = helper.createDrawable(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"),8,13, 147, 60);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("bluepower:alloyfurnace");
    }

    @Override
    public Class<? extends AlloyFurnaceWrapper> getRecipeClass() {
        return null;
    }

    @Override
    public String getTitle() {
        return "Alloy Furnace";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(AlloyFurnaceWrapper alloyFurnaceWrapper, IIngredients iIngredients) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlloyFurnaceWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        guiItemStackGroup.init(0, false, 12, 21);
        guiItemStackGroup.set(0, new ItemStack(Items.COAL));
        guiItemStackGroup.init(1, false,125, 21);
        guiItemStackGroup.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        for(int i = 0; i < 3; i++ ) {
            for(int j = 0; j < 3; j++ ) {
                guiItemStackGroup.init(i * 3 + j + 2, true, 38 + j * 18, 3 + i * 18);
            }
        }
        for(int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).get(0).size(); i++){
            guiItemStackGroup.set(i + 2, ingredients.getInputs(VanillaTypes.ITEM).get(0).get(i));
        }
    }

}

/*
class AlloyFurnaceWrapperFactory implements IRecipeWrapperFactory<StandardAlloyFurnaceRecipe> {

    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull StandardAlloyFurnaceRecipe recipe) {
        return new AlloyFurnaceWrapper(recipe);
    }
}
*/

class AlloyFurnaceWrapper { //implements IRecipeWrapper

    protected final NonNullList<Ingredient> inputs;
    protected final ItemStack output;

    public AlloyFurnaceWrapper(StandardAlloyFurnaceRecipe recipe) {
        this.inputs = recipe.getRequiredItems();
        this.output = recipe.getCraftingResult((NonNullList<ItemStack>) null);
    }

    /*@Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }*/
}

