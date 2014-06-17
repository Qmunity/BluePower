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

package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten
 */
public class ContainerSortingMachine extends Container {
    
    private final TileSortingMachine sortingMachine;
    
    private int                      pullMode, sortMode, curColumn;
    
    public ContainerSortingMachine(InventoryPlayer invPlayer, TileSortingMachine sortingMachine) {
    
        this.sortingMachine = sortingMachine;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                addSlotToContainer(new Slot(sortingMachine, i * 8 + j, 26 + j * 18, 18 + i * 18));
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
            
            if (pullMode != sortingMachine.pullMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 0, sortingMachine.pullMode.ordinal());
            }
            
            if (sortMode != sortingMachine.sortMode.ordinal()) {
                icrafting.sendProgressBarUpdate(this, 1, sortingMachine.sortMode.ordinal());
            }
            
            if (curColumn != sortingMachine.curColumn) {
                icrafting.sendProgressBarUpdate(this, 2, curColumn);
            }
        }
        
        pullMode = sortingMachine.pullMode.ordinal();
        sortMode = sortingMachine.sortMode.ordinal();
        curColumn = sortingMachine.curColumn;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
    
        if (id == 0) {
            sortingMachine.pullMode = TileSortingMachine.PullMode.values()[value];
        }
        
        if (id == 1) {
            sortingMachine.sortMode = TileSortingMachine.SortMode.values()[value];
        }
        
        if (id == 2) {
            sortingMachine.curColumn = value;
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
    
        return sortingMachine.isUseableByPlayer(entityplayer);
    }
    
}
