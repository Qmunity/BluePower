/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;

import com.bluepowermod.client.gui.GuiAlloyFurnace;
import com.bluepowermod.client.gui.GuiBlulectricAlloyFurnace;
import com.bluepowermod.client.gui.GuiBlulectricFurnace;
import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricFurnace;
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

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
        registryIn.addRecipes(getRecipes(AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE), new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registryIn.addRecipes(getMicroblockRecipes(), VanillaRecipeCategoryUid.CRAFTING);
    }

    private static List<IRecipe<?>> getRecipes(IRecipeType<?> recipeType) {
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == recipeType)
                .collect(Collectors.toList());
    }

    private static List<IRecipe<?>> getMicroblockRecipes() {
        List<IRecipe<?>> recipes = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS.getValues().stream().filter(b -> !b.hasTileEntity(b.defaultBlockState())).collect(Collectors.toList())) {
            VoxelShape shape = null;
            try{
                shape = block.defaultBlockState().getShape(null, null);
            }catch (NullPointerException ignored){
                //Shulker Boxes try to query the Tile Entity
            }
            if(block.getRegistryName() != null && shape == VoxelShapes.block()) {
                ItemStack output = ItemStack.EMPTY;
                for (Block mb : BPBlocks.microblocks){
                    NonNullList<Ingredient> input = NonNullList.create();
                    input.add(Ingredient.of(ItemTags.createOptional(new ResourceLocation("bluepower:saw"))));
                    if(mb == BPBlocks.half_block){
                        input.add(Ingredient.of(new ItemStack(block)));
                    }else{
                        input.add(Ingredient.of(output));
                    }

                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("block", block.getRegistryName().toString());
                    ItemStack stack = new ItemStack(mb);
                    stack.setTag(nbt);
                    stack.setHoverName(new TranslationTextComponent(block.getDescriptionId())
                            .append(new StringTextComponent(" "))
                            .append(new TranslationTextComponent(mb.getDescriptionId())));
                    output = stack;
                    recipes.add(new ShapelessRecipe(new ResourceLocation("bluepower:" + mb.getDescriptionId() + block.getDescriptionId()), "", output, input));
                }
            }
        }
        return recipes;
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GuiAlloyFurnace.class, 100, 32, 28, 23, new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registration.addRecipeClickArea(GuiBlulectricAlloyFurnace.class, 102, 32, 28, 23, new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registration.addRecipeClickArea(GuiBlulectricFurnace.class, 89, 32, 28, 23, VanillaRecipeCategoryUid.FURNACE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.alloyfurnace), new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.blulectric_alloyfurnace), new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.project_table), VanillaRecipeCategoryUid.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(BPBlocks.blulectric_furnace), VanillaRecipeCategoryUid.FURNACE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ContainerAlloyFurnace.class, new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME), 2, 9, 11, 36);
        registration.addRecipeTransferHandler(ContainerBlulectricAlloyFurnace.class, new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME), 1, 9, 10, 36);
        registration.addRecipeTransferHandler(ContainerProjectTable.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 54);
        registration.addRecipeTransferHandler(ContainerBlulectricFurnace.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 2, 36);
    }
}
