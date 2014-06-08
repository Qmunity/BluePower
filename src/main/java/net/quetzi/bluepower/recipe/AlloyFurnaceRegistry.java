package net.quetzi.bluepower.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRecipe;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRegistry;
import net.quetzi.bluepower.util.ItemStackUtils;

public class AlloyFurnaceRegistry implements IAlloyFurnaceRegistry{

    private static AlloyFurnaceRegistry INSTANCE = new AlloyFurnaceRegistry();
    
    private List<IAlloyFurnaceRecipe> alloyFurnaceRecipes = new ArrayList<IAlloyFurnaceRecipe>();
    
    private AlloyFurnaceRegistry(){}
    
    public static AlloyFurnaceRegistry getInstance(){
        return INSTANCE;
    }
    
    public void addRecipe(IAlloyFurnaceRecipe recipe){
        alloyFurnaceRecipes.add(recipe);
    }
    
    @Override
    public void addRecipe(ItemStack craftingResult, Object... requiredItems){
        ItemStack[] requiredStacks = new ItemStack[requiredItems.length];
        for(int i = 0; i < requiredStacks.length; i++){
            if(requiredItems[i] instanceof ItemStack){
                requiredStacks[i] = (ItemStack)requiredItems[i];
            }else if(requiredItems[i] instanceof Item){
                requiredStacks[i] = new ItemStack((Item)requiredItems[i], 1, OreDictionary.WILDCARD_VALUE);
            }else if(requiredItems[i] instanceof Block){
                requiredStacks[i] = new ItemStack((Item)requiredItems[i], 1, OreDictionary.WILDCARD_VALUE);
            }else{
                throw new IllegalArgumentException("Alloy Furnace crafting ingredients can only be ItemStack, Item or Block!");
            }
        }
        addRecipe(new StandardAlloyFurnaceRecipe(craftingResult, requiredStacks));
    }
    
    public IAlloyFurnaceRecipe getMatchingRecipe(ItemStack[] input){
        for(IAlloyFurnaceRecipe recipe : alloyFurnaceRecipes){
            if(recipe.matches(input)) return recipe;
        }
        return null;
    }   

    private class StandardAlloyFurnaceRecipe implements IAlloyFurnaceRecipe{
        private ItemStack craftingResult;
        private ItemStack[] requiredItems;
        
        private StandardAlloyFurnaceRecipe(ItemStack craftingResult, ItemStack... requiredItems){
            if(craftingResult == null) throw new IllegalArgumentException("Alloy Furnace crafting result can't be null!");
            for(ItemStack requiredItem : requiredItems){
                if(requiredItem == null) throw new NullPointerException("An Alloy Furnace crafting ingredient can't be null!");
            }
            for(ItemStack stack : requiredItems){
                for(ItemStack stack2 : requiredItems){
                    if(stack != stack2 && ItemStackUtils.isItemFuzzyEqual(stack, stack2)) throw new IllegalArgumentException("No equivalent Alloy Furnace crafting ingredient can be given twice! This does take OreDict + wildcard values in account.");
                }
            }
            
            this.craftingResult = craftingResult;
            this.requiredItems = requiredItems;
        }
        
        @Override
        public boolean matches(ItemStack[] input){
            for(ItemStack requiredItem : requiredItems){
                int itemsNeeded = requiredItem.stackSize;
                for(ItemStack inputStack : input){
                    if(inputStack != null && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)){
                        itemsNeeded -= inputStack.stackSize;
                        if(itemsNeeded <= 0) break;
                    }
                }
                if(itemsNeeded > 0) return false;
            }
            return true;
        }

        @Override
        public void useItems(ItemStack[] input){
            for(ItemStack requiredItem : requiredItems){
                int itemsNeeded = requiredItem.stackSize;
                for(int i = 0; i < input.length; i++){
                    ItemStack inputStack = input[i];
                    if(inputStack != null && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)){
                        int itemsSubstracted = Math.min(inputStack.stackSize, itemsNeeded);
                        inputStack.stackSize -= itemsSubstracted;
                        if(inputStack.stackSize <= 0) input[i] = null;
                        itemsNeeded -= itemsSubstracted;
                        if(itemsNeeded <= 0) break;
                    }
                }
                if(itemsNeeded > 0) throw new IllegalArgumentException("Alloy Furnace recipe using items, after using still items required?? This is a bug!");
            }
        }

        @Override
        public ItemStack getCraftingResult(ItemStack[] input){
            return craftingResult;
        }
        
    }
}
