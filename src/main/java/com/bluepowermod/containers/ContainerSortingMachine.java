/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.containers.slots.IPhantomSlot;
import com.bluepowermod.containers.slots.SlotPhantom;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten
 */
public class ContainerSortingMachine extends Container {
    
    private final TileSortingMachine sortingMachine;
    
    private int                      pullMode, sortMode, curColumn = -1;
    private final int[]              colors = new int[9];
    
    public ContainerSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {
    
        this.sortingMachine = sortingMachine;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                addSlotToContainer(new SlotPhantom(sortingMachine, i * 8 + j, 26 + j * 18, 18 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        int offset = 140;
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, offset + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 58 + offset));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    
        ItemStack var3 = null;
        Slot var4 = (Slot) inventorySlots.get(par2);
        
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            
            if (par2 < 40) {
                if (!mergeItemStack(var5, 40, 76, false)) return null;
                var4.onSlotChange(var5, var3);
            } else {
                if (!mergeItemStack(var5, 0, 40, false)) return null;
                var4.onSlotChange(var5, var3);
            }
            
            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }
            
            if (var5.stackSize == var3.stackSize) return null;
            
            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }
        
        return var3;
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
    
        super.detectAndSendChanges();
        
        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            
            for (int i = 0; i < 9; i++) {
                if (colors[i] != sortingMachine.colors[i].ordinal()) {
                    icrafting.sendProgressBarUpdate(this, i, sortingMachine.colors[i].ordinal());
                }
            }
            
            if (pullMode != sortingMachine.pullMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 9, sortingMachine.pullMode.ordinal());
            }
            
            if (sortMode != sortingMachine.sortMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 10, sortingMachine.sortMode.ordinal());
            }
            
            if (curColumn != sortingMachine.curColumn) {
                icrafting.sendProgressBarUpdate(this, 11, sortingMachine.curColumn);
            }
        }
        
        pullMode = sortingMachine.pullMode.ordinal();
        sortMode = sortingMachine.sortMode.ordinal();
        curColumn = sortingMachine.curColumn;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = sortingMachine.colors[i].ordinal();
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
    
        if (id < 9) {
            sortingMachine.colors[id] = TubeColor.values()[value];
        }
        
        if (id == 9) {
            sortingMachine.pullMode = TileSortingMachine.PullMode.values()[value];
        }
        
        if (id == 10) {
            sortingMachine.sortMode = TileSortingMachine.SortMode.values()[value];
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
        
        if (id == 11) {
            sortingMachine.curColumn = value;
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
    
        return sortingMachine.isUseableByPlayer(entityplayer);
    }
    
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
