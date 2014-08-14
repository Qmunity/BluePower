package com.bluepowermod.api.item;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Implemented by items/blocks/BPParts that can be copied and shared in a Circuit Database.
 * When copied, the entire NBTTag will be copied to the outputStack.
 * @author MineMaarten
 */
public interface IDatabaseSaveable {
    
    /**
     * Return true if the ItemStack that's being 'injected' with info is a stack that can be injected.
     * This method is only called when itemStack.isItemEqual(otherStack) returned true.
     * @param outputStack
     * @return false to disallow copying.
     */
    public boolean canCopyInto(ItemStack outputStack);
    
    /**
     * Items that contain items (an Integrated Circuit with gates on it) need to compare the input and output, and tell
     * which items are required. With this method you can tell the Circuit Database what items the item carries, so it can
     * calculate which items it needs.
     * @param templateStack
     * @param outputStack
     * @return null is a valid return.
     */
    public List<ItemStack> getItemsOnStack(ItemStack stack);
    
}
