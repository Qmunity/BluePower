/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.container.slot.IPhantomSlot;

/**
 * @author MineMaarten
 */
public abstract class ContainerGhosts extends Container {
    
    /**
     * This class is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    @Override
    public ItemStack slotClick(int slotNum, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        Slot slot = slotNum < 0 ? null : (Slot) inventorySlots.get(slotNum);
        if (slot instanceof IPhantomSlot) { return slotClickPhantom(slot, dragType, clickTypeIn,  player); }
        return super.slotClick(slotNum, dragType, clickTypeIn, player);
    }

    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    private ItemStack slotClickPhantom(Slot slot, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    
        ItemStack stack = ItemStack.EMPTY;
        
        if (clickTypeIn.ordinal() == 2) {
            if (((IPhantomSlot) slot).canAdjust()) {
                slot.putStack(null);
            }
        } else if (clickTypeIn.ordinal() == 0 || clickTypeIn.ordinal() == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();
            
            if (stackSlot != ItemStack.EMPTY) {
                stack = stackSlot.copy();
            }
            
            if (stackSlot == ItemStack.EMPTY) {
                if (stackHeld != ItemStack.EMPTY && slot.isItemValid(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, clickTypeIn.ordinal(), dragType);
                }
            } else if (stackHeld == ItemStack.EMPTY) {
                adjustPhantomSlot(slot, clickTypeIn.ordinal(), dragType);
                slot.onSlotChange(stack, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (canStacksMerge(stackSlot, stackHeld)) {
                    adjustPhantomSlot(slot, clickTypeIn.ordinal(), dragType);
                } else {
                    fillPhantomSlot(slot, stackHeld, clickTypeIn.ordinal(), dragType);
                }
            }
        }
        return stack;
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    public boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
    
        if (stack1 == null || stack2 == null) return false;
        if (!stack1.isItemEqual(stack2)) return false;
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2)) return false;
        return true;
        
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
    
        if (!((IPhantomSlot) slot).canAdjust()) { return; }
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1) {
            stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
        }
        
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        
        stackSlot.setCount(stackSize);
        
        if (stackSlot.getCount() <= 0) {
            slot.putStack((ItemStack) null);
        }
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
    
        if (!((IPhantomSlot) slot).canAdjust()) { return; }
        int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.setCount(stackSize);
        
        slot.putStack(phantomStack);
    }
    
}
