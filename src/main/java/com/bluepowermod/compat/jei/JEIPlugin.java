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
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * @author MoreThanHidden
 */
/*
TODO: waiting for JEI 1.17
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
        registryIn.addRecipes(getRecipes(AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE).stream().filter(recipe -> recipe instanceof AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe).collect(Collectors.toSet()), new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
        registryIn.addRecipes(getMicroblockRecipes(), VanillaRecipeCategoryUid.CRAFTING);
        registryIn.addRecipes(getRecyclingRecipes(), new ResourceLocation(Refs.MODID, Refs.ALLOYFURNACE_NAME));
    }

    private static List<IRecipe<?>> getRecipes(IRecipeType<?> recipeType) {
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == recipeType)
                .collect(Collectors.toList());
    }

    private static List<IRecipe<?>> getMicroblockRecipes() {
        List<IRecipe<?>> recipes = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS) {
            VoxelShape shape = null;
            try{
                shape = block.defaultBlockState().getShape(null, null);
            }catch (NullPointerException ignored){
                //Shulker Boxes try to query the Tile Entity
            }
            if(block.getRegistryName() != null && shape == Shapes.block()) {
                ItemStack output = ItemStack.EMPTY;
                for (Block mb : BPBlocks.microblocks){
                    NonNullList<Ingredient> input = NonNullList.create();
                    input.add(Ingredient.of(ItemTags.createOptional(new ResourceLocation("bluepower:saw"))));
                    if(mb == BPBlocks.half_block){
                        input.add(Ingredient.of(new ItemStack(block)));
                    }else{
                        input.add(Ingredient.of(output));
                    }

                    CompoundTag nbt = new CompoundTag();
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


    private static List<Recipe<?>> getRecyclingRecipes() {
        List<Recipe<?>> recipesList = new ArrayList<>();

        for (ItemStack outputItem : AlloyFurnaceRegistry.getInstance().recyclingItems){

            //Build the blacklist based on config
            Set<Item> blacklist = new HashSet<>(AlloyFurnaceRegistry.getInstance().blacklist);

            for (Recipe<?> recipe : getRecipes(RecipeType.CRAFTING)) {
                if (recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(outputItem))) {
                    int recyclingAmount = 0;
                    ItemStack currentlyRecycledInto = ItemStack.EMPTY;

                    for (ItemStack recyclingItem : AlloyFurnaceRegistry.getInstance().recyclingItems) {
                        try {
                            if (recipe instanceof ICraftingRecipe) {
                                if (!recipe.getIngredients().isEmpty()) {
                                    for (Ingredient input : recipe.getIngredients()) {
                                        if (!input.isEmpty()) {
                                            //Serialize and Deserialize the Object so the base tag isn't affected.
                                            Ingredient ingredient = Ingredient.fromJson(input.toJson());
                                            if (ingredient.test(recyclingItem)) {
                                                ItemStack moltenDownItem = AlloyFurnaceRegistry.getInstance().getRecyclingStack(recyclingItem);
                                                if (currentlyRecycledInto.isEmpty()
                                                        || ItemStackUtils.isItemFuzzyEqual(currentlyRecycledInto, moltenDownItem)) {
                                                    currentlyRecycledInto = moltenDownItem;
                                                    recyclingAmount += moltenDownItem.getCount();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            BluePower.log.error("Error when generating an Alloy Furnace recipe for item " + recyclingItem.getDisplayName().getString()
                                    + ", recipe output: " + recipe.getResultItem().getDisplayName().getString());
                            e.printStackTrace();
                        }
                    }


                    if (recyclingAmount > 0 && recipe.getResultItem().getCount() > 0) {
                        //Try to avoid Duping
                        if (!blacklist.contains(recipe.getResultItem().getItem()) && recipe.getResultItem().getCount() > recyclingAmount) {
                            blacklist.add(recipe.getResultItem().getItem());
                        }

                        //Skip item if it is on the blacklist
                        if (blacklist.contains(recipe.getResultItem().getItem())) {
                            continue;
                        }

                        //Divide by the Recipe Output
                        ItemStack output = new ItemStack(currentlyRecycledInto.getItem(), Math.min(64, recyclingAmount / recipe.getResultItem().getCount()));
                        recipesList.add(new AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe(new ResourceLocation("bluepower:" + output.getItem().getRegistryName().toString().replace(":",".") + recipe.getResultItem().getItem().getRegistryName().toString().replace(":",".")), "", output, NonNullList.of(Ingredient.of(recipe.getResultItem()), Ingredient.of(recipe.getResultItem())), NonNullList.of(0, 1)));
                    }
                }
            }
        }

        return recipesList;
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
*/
