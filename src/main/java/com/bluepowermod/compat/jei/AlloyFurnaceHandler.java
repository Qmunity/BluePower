/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

/**
  @author MoreThanHidden
*/

public class AlloyFurnaceHandler implements IRecipeCategory<IAlloyFurnaceRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    public static final RecipeType<IAlloyFurnaceRecipe> RECIPE_TYPE = new RecipeType<>(new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME), IAlloyFurnaceRecipe.class);

    public AlloyFurnaceHandler(IGuiHelper helper) {
      background = helper.createDrawable(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"),8,13, 147, 60);
      icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BPBlocks.alloyfurnace.get()));
      progress = helper.drawableBuilder(new ResourceLocation(Refs.MODID + ":textures/gui/alloy_furnace.png"), 178, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<IAlloyFurnaceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.bluepower." + Refs.ALLOYFURNACE_NAME);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void draw(IAlloyFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        progress.draw(matrixStack, 95, 22);
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IAlloyFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 22).addItemStack(recipe.getResultItem());
        int count = 0;
        for(int i = 0; i < 3; i++ ) {
            for(int j = 0; j < 3; j++ ) {
                IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, 39 + j * 18, 4 + i * 18);
                if(recipe.getRequiredItems() != null && count < recipe.getRequiredItems().size()) {
                    int finalCount = count;
                    slot.addItemStacks(Arrays.stream(recipe.getRequiredItems().get(count).getItems()).peek((stack) ->
                            stack.setCount(recipe.getRequiredCount().get(finalCount))).toList());
                    count++;
                }
            }
        }
    }

}
