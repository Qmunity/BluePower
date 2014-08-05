package com.bluepowermod.items;

import net.minecraft.item.ItemStack;

public class ItemPaintCan extends ItemDamageableColorableOverlay {
    
    private static final int MAX_USES = 16;
    
    public ItemPaintCan(String name) {
    
        super(name);
    }
    
    @Override
    protected int getMaxUses() {
    
        return MAX_USES;
    }
    
    /**
     * ItemStack sensitive version of getContainerItem.
     * Returns a full ItemStack instance of the result.
     *
     * @param itemStack The current ItemStack
     * @return The resulting ItemStack
     */
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
    
        if (itemStack.getItemDamage() >= 16) return null;
        ItemStack stack = itemStack.copy();
        int newUses = getUsesUsed(stack) + 1;
        
        if (newUses == getMaxUses()) {
            stack.setItemDamage(16);
            setUsesUsed(stack, 0);
        } else {
            setUsesUsed(stack, newUses);
        }
        return stack;
    }
    
    /**
     * ItemStack sensitive version of hasContainerItem
     * @param stack The current item stack
     * @return True if this item has a 'container'
     */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
    
        return true;
    }
}
