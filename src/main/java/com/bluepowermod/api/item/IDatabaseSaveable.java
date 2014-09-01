/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.item;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Implemented by items/blocks that can be copied and shared in a Circuit Database.
 * When copied, the entire NBTTag will be copied to the outputStack.
 * @author MineMaarten
 */
public interface IDatabaseSaveable {
    
    /**
     * First layer of checking if the item is valid, when the item is being tried to be placed in the copy/share slot, or output slot.
     * @param stack
     * @return
     */
    public boolean canGoInCopySlot(ItemStack stack);
    
    /**
     * Return true if the ItemStack that's being 'injected' with info is a stack that can be injected.
     * This method is only called when templateStack.isItemEqual(outputStack) returned true.
     * @param templateStack
     * @param outputStack
     * @return false to disallow copying.
     */
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack);
    
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
