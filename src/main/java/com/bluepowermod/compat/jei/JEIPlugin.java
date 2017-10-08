/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MoreThanHidden
 */
@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelpers;
    public static IModRegistry modRegistry;
    Map<Class, IRecipeCategory> categories = new LinkedHashMap<>();

    @Override
    public void  registerCategories(IRecipeCategoryRegistration registry) {

        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        categories.put(AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe.class, new AlloyFurnaceHandler(guiHelper));
        registry.addRecipeCategories(categories.values().toArray(new IRecipeCategory[categories.size()]));

    }

    @Override
    public void register(IModRegistry registryIn) {
        modRegistry = registryIn;
        //Alloy Furnace
        modRegistry.addRecipes( AlloyFurnaceRegistry.getInstance().getAllRecipes(), "bluepower.alloyfurnace");
        modRegistry.handleRecipes(AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe.class, new AlloyFurnaceWrapperFactory(), "bluepower.alloyfurnace");
        modRegistry.addRecipeCatalyst(new ItemStack(BPBlocks.alloyfurnace), "bluepower.alloyfurnace");
        //Crafting Table
        modRegistry.addRecipeCatalyst(new ItemStack(BPBlocks.project_table), VanillaRecipeCategoryUid.CRAFTING);
    }

}
