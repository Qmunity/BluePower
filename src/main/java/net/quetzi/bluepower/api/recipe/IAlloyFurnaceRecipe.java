package net.quetzi.bluepower.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * This class only should be used for special recipes, like recipes taht depend on NBT. For normal recipes use 
 * {@link net.quetzi.bluepower.recipe.AlloyFurnaceRegistry#addRecipe(ItemStack, ItemStack...)}.
 */
public interface IAlloyFurnaceRecipe{

    /**
     * Return true if this recipe can be smelted using the input stacks. The input stacks are the 9 inventory slots, so an element can be null.
     * @param input
     * @return
     */
    boolean matches(ItemStack[] input);

    /**
     * The items that are needed in this recipe need to be removed from the input inventory.
     */
    void useItems(ItemStack[] input);
    
    ItemStack getCraftingResult(ItemStack[] input);
}
