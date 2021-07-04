/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.container.slot.IPhantomSlot;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */
public abstract class ContainerGhosts extends Container {

    protected ContainerGhosts(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
    }

    /**
     * This class is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    @Override
    public ItemStack clicked(int slotNum, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        Slot slot = slotNum < 0 ? null : (Slot) slots.get(slotNum);
        if (slot instanceof IPhantomSlot) { return clickedPhantom(slot, dragType, clickTypeIn,  player); }
        return super.clicked(slotNum, dragType, clickTypeIn, player);
    }

    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    private ItemStack clickedPhantom(Slot slot, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    
        ItemStack stack = ItemStack.EMPTY;
        
        if (clickTypeIn.ordinal() == 2) {
            if (((IPhantomSlot) slot).canAdjust()) {
                slot.set(ItemStack.EMPTY);
            }
        } else if (clickTypeIn.ordinal() == 0 || clickTypeIn.ordinal() == 1) {
            PlayerInventory playerInv = player.inventory;
            slot.setChanged();
            ItemStack stackSlot = slot.getItem();
            ItemStack stackHeld = playerInv.getCarried();
            
            if (stackSlot != ItemStack.EMPTY) {
                stack = stackSlot.copy();
            }
            
            if (stackSlot == ItemStack.EMPTY) {
                if (stackHeld != ItemStack.EMPTY && slot.mayPlace(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, clickTypeIn.ordinal(), dragType);
                }
            } else if (stackHeld == ItemStack.EMPTY) {
                adjustPhantomSlot(slot, clickTypeIn.ordinal(), dragType);
                slot.onQuickCraft(stack, playerInv.getCarried());
            } else if (slot.mayPlace(stackHeld)) {
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
    
        if (stack1.isEmpty() || stack2.isEmpty()) return false;
        if (!stack1.sameItem(stack2)) return false;
        if (!ItemStack.tagMatches(stack1, stack2)) return false;
        return true;
        
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
    
        if (!((IPhantomSlot) slot).canAdjust()) { return; }
        ItemStack stackSlot = slot.getItem();
        int stackSize;
        if (modifier == 1) {
            stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
        }
        
        if (stackSize > slot.getMaxStackSize()) {
            stackSize = slot.getMaxStackSize();
        }
        
        stackSlot.setCount(stackSize);
        
        if (!stackSlot.isEmpty() && stackSlot.getCount() <= 0) {
            slot.set(ItemStack.EMPTY);
        }
    }
    
    /**
     * This method is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
     * @author CovertJaguar <http://www.railcraft.info>
     */
    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
    
        if (!((IPhantomSlot) slot).canAdjust()) { return; }
        int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
        if (stackSize > slot.getMaxStackSize()) {
            stackSize = slot.getMaxStackSize();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.setCount(stackSize);
        
        slot.set(phantomStack);
    }
    
}
