/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.client.gui.BPMenuType;
import com.bluepowermod.client.gui.GuiAlloyFurnace;
import com.bluepowermod.client.gui.GuiBlulectricAlloyFurnace;
import com.bluepowermod.client.gui.GuiBlulectricFurnace;
import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricFurnace;
import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPRecipeTypes;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.reference.Refs;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        registry.addRecipeCategories(categories.values().toArray(new IRecipeCategory[categories.size()]));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registryIn) {
        registryIn.addRecipes(AlloyFurnaceHandler.RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(BPRecipeTypes.ALLOY_SMELTING.get()).stream().filter(r -> !r.getResultItem().isEmpty()).toList());
        registryIn.addRecipes(RecipeTypes.CRAFTING, getMicroblockRecipes());
        registryIn.addRecipes(AlloyFurnaceHandler.RECIPE_TYPE, getRecyclingRecipes());
    }

    private static List<CraftingRecipe> getMicroblockRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS.getValues().stream().filter(b -> !(b instanceof EntityBlock)).collect(Collectors.toList())) {
            VoxelShape shape = null;
            try{
                shape = block.defaultBlockState().getShape(null, null);
            }catch (NullPointerException ignored){
                //Shulker Boxes try to query the Tile Entity
            }
            if(ForgeRegistries.BLOCKS.getKey(block) != null && shape == Shapes.block()) {
                ItemStack output = ItemStack.EMPTY;
                for (RegistryObject<Block> mb : BPBlocks.microblocks){
                    NonNullList<Ingredient> input = NonNullList.create();
                    input.add(Ingredient.of(ItemTags.create(new ResourceLocation("bluepower:saw"))));
                    if(mb == BPBlocks.half_block){
                        input.add(Ingredient.of(new ItemStack(block)));
                    }else{
                        input.add(Ingredient.of(output));
                    }

                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("block", ForgeRegistries.BLOCKS.getKey(block).toString());
                    ItemStack stack = new ItemStack(mb.get());
                    stack.setTag(nbt);
                    stack.setHoverName(Component.translatable(block.getDescriptionId())
                            .append(Component.literal(" "))
                            .append(Component.translatable(mb.get().getDescriptionId())));
                    output = stack;
                    recipes.add(new ShapelessRecipe(new ResourceLocation("bluepower:" + mb.get().getDescriptionId() + block.getDescriptionId()), "", output, input));
                }
            }
        }
        return recipes;
    }


    private static List<IAlloyFurnaceRecipe> getRecyclingRecipes() {
        List<IAlloyFurnaceRecipe> recipesList = new ArrayList<>();

        for (Map.Entry<Item, ItemStack> recipe : AlloyFurnaceRegistry.getInstance().recyclingRecipes.entrySet()) {
            recipesList.add(new AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe(new ResourceLocation("bluepower:" + ForgeRegistries.ITEMS.getKey(recipe.getValue().getItem()).toString().replace(":", ".") + ForgeRegistries.ITEMS.getKey(recipe.getKey()).toString().replace(":", ".")), "", recipe.getValue(), NonNullList.of(Ingredient.of(recipe.getKey()), Ingredient.of(recipe.getKey())), NonNullList.of(0, 1)));
        }

        return recipesList;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GuiAlloyFurnace.class, 100, 32, 28, 23, AlloyFurnaceHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(GuiBlulectricAlloyFurnace.class, 102, 32, 28, 23, AlloyFurnaceHandler.RECIPE_TYPE);
        registration.addRecipeClickArea(GuiBlulectricFurnace.class, 89, 32, 28, 23, RecipeTypes.SMELTING);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.alloyfurnace.get()), AlloyFurnaceHandler.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.blulectric_alloyfurnace.get()), AlloyFurnaceHandler.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.project_table.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.blulectric_furnace.get()), RecipeTypes.SMELTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ContainerAlloyFurnace.class, BPMenuType.ALLOY_FURNACE.get(), AlloyFurnaceHandler.RECIPE_TYPE, 2, 9, 11, 36);
        registration.addRecipeTransferHandler(ContainerBlulectricAlloyFurnace.class, BPMenuType.BLULECTRIC_ALLOY_FURNACE.get(), AlloyFurnaceHandler.RECIPE_TYPE, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(ContainerProjectTable.class, BPMenuType.PROJECT_TABLE.get(), RecipeTypes.CRAFTING, 1, 9, 10, 54);
        registration.addRecipeTransferHandler(ContainerBlulectricFurnace.class, BPMenuType.BLULECTRIC_FURNACE.get(), RecipeTypes.SMELTING, 0, 1, 2, 36);
    }
}
