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
import com.sun.xml.internal.bind.v2.model.core.ID;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
  @author MoreThanHidden
*/


public class AlloyFurnaceHandler implements IRecipeCategory<AlloyFurnaceWrapper> {

    private final IDrawable background;

    public AlloyFurnaceHandler(IGuiHelper helper) {
      background = helper.createDrawable(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"),8,13, 142, 60);
    }

    @Override
    public String getUid() {
        return "bluepower.alloyfurnace";
    }

    @Override
    public String getTitle() {
        return "Alloy Furnace";
    }

    @Override
    public String getModName() {
        return Refs.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlloyFurnaceWrapper recipeWrapper, IIngredients ingredients) {

    }

}

class AlloyFurnaceWrapperFactory implements IRecipeWrapperFactory<StandardAlloyFurnaceRecipe> {

    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull StandardAlloyFurnaceRecipe recipe) {
        return new AlloyFurnaceWrapper(recipe);
    }
}

class AlloyFurnaceWrapper implements IRecipeWrapper {

    protected final NonNullList<ItemStack> inputs;
    protected final ItemStack output;

    public AlloyFurnaceWrapper(StandardAlloyFurnaceRecipe recipe) {
        this.inputs = recipe.getRequiredItems();
        this.output = recipe.getCraftingResult(inputs);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }
}

