/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.recipe.AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe;
import com.bluepowermod.reference.Refs;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
  @author MoreThanHidden
*/


public class AlloyFurnaceHandler implements IRecipeCategory<StandardAlloyFurnaceRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;

    public AlloyFurnaceHandler(IGuiHelper helper) {
      background = helper.createDrawable(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"),8,13, 147, 60);
      icon = helper.createDrawableIngredient(new ItemStack(BPBlocks.alloyfurnace));
      progress = helper.drawableBuilder(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"), 178, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME);
    }

    @Override
    public Class<? extends StandardAlloyFurnaceRecipe> getRecipeClass() {
        return StandardAlloyFurnaceRecipe.class;
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
    public void draw(StandardAlloyFurnaceRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        progress.draw(matrixStack, 95, 22);
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(StandardAlloyFurnaceRecipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> items = recipe.getRequiredItems().stream().map(ingredient -> Arrays.asList(ingredient.getMatchingStacks())).collect(Collectors.toList());
        for (int i = 0; i < items.size(); i++) {
            for(ItemStack itemStack : items.get(i)){
                itemStack.setCount(recipe.getRequiredCount().get(i));
            }
        }
        ingredients.setInputLists(VanillaTypes.ITEM, items);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, StandardAlloyFurnaceRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
        guiItemStackGroup.init(0, false, 12, 21);
        guiItemStackGroup.set(0, new ItemStack(Items.COAL));
        guiItemStackGroup.init(1, false,125, 21);
        guiItemStackGroup.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        for(int i = 0; i < 3; i++ ) {
            for(int j = 0; j < 3; j++ ) {
                guiItemStackGroup.init(i * 3 + j + 2, true, 38 + j * 18, 3 + i * 18);
            }
        }
        for(int i = 0; i < recipe.getRequiredItems().size(); i++){
            List<ItemStack> itemStacks = ingredients.getInputs(VanillaTypes.ITEM).get(i);
            for (ItemStack itemStack : itemStacks) {
                itemStack.setCount(recipe.getRequiredCount().get(i));
            }
            guiItemStackGroup.set(i + 2, itemStacks);
        }
    }
}
