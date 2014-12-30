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
    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
    
        Slot slot = slotNum < 0 ? null : (Slot) inventorySlots.get(slotNum);
        if (slot instanceof IPhantomSlot) { return slotClickPhantom(slot, mouseButton, modifier, player); }
        return super.slotClick(slotNum, mouseButton, modifier, player);
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
    
        ItemStack stack = null;
        
        if (mouseButton == 2) {
            if (((IPhantomSlot) slot).canAdjust()) {
                slot.putStack(null);
            }
        } else if (mouseButton == 0 || mouseButton == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();
            
            if (stackSlot != null) {
                stack = stackSlot.copy();
            }
            
            if (stackSlot == null) {
                if (stackHeld != null && slot.isItemValid(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
                }
            } else if (stackHeld == null) {
                adjustPhantomSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (canStacksMerge(stackSlot, stackHeld)) {
                    adjustPhantomSlot(slot, mouseButton, modifier);
                } else {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
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
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        }
        
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        
        stackSlot.stackSize = stackSize;
        
        if (stackSlot.stackSize <= 0) {
            slot.putStack((ItemStack) null);
        }
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
    
        if (!((IPhantomSlot) slot).canAdjust()) { return; }
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;
        
        slot.putStack(phantomStack);
    }
    
}
