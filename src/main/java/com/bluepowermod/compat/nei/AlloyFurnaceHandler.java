package com.bluepowermod.compat.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.client.gui.GuiAlloyFurnace;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.recipe.AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe;
import com.bluepowermod.references.Refs;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;

/**
 * 
 * @author MineMaarten
 */

public class AlloyFurnaceHandler extends FurnaceRecipeHandler {
    
    @Override
    public String getRecipeName() {
    
        return "Alloy Furnace";
    }
    
    @Override
    public String getGuiTexture() {
    
        return Refs.MODID + ":textures/GUI/alloy_furnace.png";
    }
    
    /**
     * @return The class of the GuiContainer that this recipe would be crafted in.
     */
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
    
        return GuiAlloyFurnace.class;
    }
    
    /**
     * Loads a rectangle that can be clicked in the Alloy Furnace GUI that will load up every recipe of the Alloy Furnace
     */
    @Override
    public void loadTransferRects() {
    
        transferRects.add(new RecipeTransferRect(new Rectangle(17, 43, 18, 18), "fuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(97, 24, 22, 14), getRecipesID()));
    }
    
    @Override
    public void drawExtras(int recipe) {
    
        drawProgressBar(17, 43, 177, 0, 14, 14, 48, 7);
        drawProgressBar(97, 24, 177, 14, 24, 16, 48, 0);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
    
        if (outputId.equals(getRecipesID())) {
            for (IAlloyFurnaceRecipe recipe : AlloyFurnaceRegistry.getInstance().getAllRecipes())
                if (recipe instanceof AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) arecipes.add(new AlloyRecipe((AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) recipe));
        } else if (outputId.equals("fuel")) {
            
        } else super.loadCraftingRecipes(outputId, results);
    }
    
    private String getRecipesID() {
    
        return "alloyFurnace";
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
    
        for (IAlloyFurnaceRecipe recipe : AlloyFurnaceRegistry.getInstance().getAllRecipes())
            if (recipe instanceof AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) {
                if (NEIClientUtils.areStacksSameTypeCrafting(recipe.getCraftingResult(null), result)) {
                    arecipes.add(new AlloyRecipe((AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) recipe));
                }
            }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
    
        for (IAlloyFurnaceRecipe recipe : AlloyFurnaceRegistry.getInstance().getAllRecipes()) {
            if (recipe instanceof AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) {
                AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe standardAlloyRecipe = (AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) recipe;
                for (ItemStack input : standardAlloyRecipe.getRequiredItems()) {
                    if (NEIClientUtils.areStacksSameTypeCrafting(input, ingredient)) {
                        arecipes.add(new AlloyRecipe(standardAlloyRecipe));
                        break;
                    }
                }
            }
        }
    }
    
    public class AlloyRecipe extends CachedRecipe {
        
        private final PositionedStack       craftingResult;
        private final List<PositionedStack> requiredItems;
        
        public AlloyRecipe(StandardAlloyFurnaceRecipe alloyRecipe) {
        
            craftingResult = new PositionedStack(alloyRecipe.getCraftingResult(null), 129, 24);
            requiredItems = new ArrayList<PositionedStack>();
            int x = 0, y = 0;
            for (ItemStack requiredItem : alloyRecipe.getRequiredItems()) {
                requiredItems.add(new PositionedStack(requiredItem, 42 + x * 18, 6 + y * 18));
                if (++x > 2) {
                    x = 0;
                    y++;
                }
            }
        }
        
        @Override
        public PositionedStack getResult() {
        
            return craftingResult;
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
        
            return requiredItems;
        }
        
        @Override
        public PositionedStack getOtherStack() {
        
            PositionedStack stack = afuels.get(cycleticks / 48 % afuels.size()).stack;
            stack = stack.copy();
            stack.relx = 16;
            stack.rely = 24;
            return stack;
        }
    }
}
