package net.quetzi.bluepower.compat.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRecipe;
import net.quetzi.bluepower.recipe.AlloyFurnaceRegistry;
import net.quetzi.bluepower.recipe.AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe;
import net.quetzi.bluepower.references.Refs;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class AlloyFurnaceHandler extends TemplateRecipeHandler {
    
    @Override
    public String getRecipeName() {
    
        return "Alloy Furnace";
    }
    
    @Override
    public String getGuiTexture() {
    
        return Refs.MODID + ":textures/GUI/alloy_furnace.png";
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
    
        if (outputId.equals(getRecipesID())) {
            for (IAlloyFurnaceRecipe recipe : AlloyFurnaceRegistry.getInstance().getAllRecipes())
                if (recipe instanceof AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) arecipes.add(new AlloyRecipe((AlloyFurnaceRegistry.StandardAlloyFurnaceRecipe) recipe));
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
    }
}
