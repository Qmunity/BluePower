package net.quetzi.bluepower.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtils {
    
    public static boolean isItemFuzzyEqual(ItemStack stack1, ItemStack stack2) {
    
        if (stack1.getItem() != stack2.getItem() && !isSameOreDictStack(stack1, stack2)) return false;
        return stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }
    
    public static boolean isSameOreDictStack(ItemStack stack1, ItemStack stack2) {
    
        List<ItemStack> oreDictStacks = OreDictionary.getOres(OreDictionary.getOreID(stack1));
        for (ItemStack oreDictStack : oreDictStacks) {
            if (OreDictionary.itemMatches(oreDictStack, stack2, false)) { return true; }
        }
        return false;
    }
}
