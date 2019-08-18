/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.reference.Refs;
import mezz.jei.api.*;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MoreThanHidden
 */
@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelpers;
    Map<Class, IRecipeCategory> categories = new LinkedHashMap<>();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Refs.MODID, "jeiplugin");
    }

    @Override
    public void  registerCategories(IRecipeCategoryRegistration registry) {

        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        categories.put(AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe.class, new AlloyFurnaceHandler(guiHelper));
        //registry.addRecipeCategories(categories.values().toArray(new IRecipeCategory[categories.size()]));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registryIn) {
        //registryIn.addRecipes( AlloyFurnaceRegistry.getInstance().getAllRecipes(), new ResourceLocation("bluepower:alloyfurnace"));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //TODO: JEI Recipe Handler
        //registration.handleRecipes(AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe.class, new AlloyFurnaceWrapperFactory(), "bluepower.alloyfurnace");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.alloyfurnace), new ResourceLocation("bluepower:alloyfurnace"));
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.project_table), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ContainerAlloyFurnace.class, new ResourceLocation("bluepower:alloyfurnace"), 2, 9, 11, 36);
        registration.addRecipeTransferHandler(ContainerProjectTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 54);
    }
}
