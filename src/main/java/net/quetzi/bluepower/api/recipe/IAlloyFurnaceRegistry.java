package net.quetzi.bluepower.api.recipe;

import net.minecraft.item.ItemStack;

public interface IAlloyFurnaceRegistry {
    
    void addRecipe(IAlloyFurnaceRecipe recipe);
    
    void addRecipe(ItemStack output, Object... input);
}
