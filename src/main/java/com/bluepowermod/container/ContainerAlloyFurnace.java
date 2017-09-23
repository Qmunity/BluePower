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

package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ContainerAlloyFurnace extends Container {
    
    private final TileAlloyFurnace tileFurnace;
    
    private int                    currentBurnTime;
    private int                    maxBurnTime;
    private int                    currentProcessTime;
    
    public ContainerAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace furnace) {
    
        tileFurnace = furnace;
        
        addSlotToContainer(new SlotMachineInput(furnace, 0, 21, 35));
        addSlotToContainer(new SlotMachineOutput(furnace, 1, 134, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotMachineInput(furnace, i * 3 + j + 2, 47 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = inventorySlots.get(par2);
        
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            
            if (par2 < 11) {
                if (!mergeItemStack(var5, 11, 47, false)) return ItemStack.EMPTY;
                var4.onSlotChange(var5, var3);
            } else {
                if (TileEntityFurnace.isItemFuel(var5) && mergeItemStack(var5, 0, 1, false)) {
                    
                } else if (!mergeItemStack(var5, 2, 11, false)) return ItemStack.EMPTY;
                var4.onSlotChange(var5, var3);
            }
            
            if (var5.getCount() == 0) {
                var4.putStack(ItemStack.EMPTY);
            } else {
                var4.onSlotChanged();
            }
            
            if (var5.getCount() == var3.getCount()) return ItemStack.EMPTY;
            
            var4.onSlotChange(var3, var5);
        }
        
        return var3;
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        
        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;
            
            if (currentBurnTime != tileFurnace.currentBurnTime) {
                icrafting.sendWindowProperty(this, 0, tileFurnace.currentBurnTime);
            }
            
            if (maxBurnTime != tileFurnace.maxBurnTime) {
                icrafting.sendWindowProperty(this, 1, tileFurnace.maxBurnTime);
            }
            
            if (currentProcessTime != tileFurnace.currentProcessTime) {
                icrafting.sendWindowProperty(this, 2, tileFurnace.currentProcessTime);
            }
        }
        
        currentBurnTime = tileFurnace.currentBurnTime;
        maxBurnTime = tileFurnace.maxBurnTime;
        currentProcessTime = tileFurnace.currentProcessTime;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    
        if (par1 == 0) {
            tileFurnace.currentBurnTime = par2;
        }
        
        if (par1 == 1) {
            tileFurnace.maxBurnTime = par2;
        }
        
        if (par1 == 2) {
            tileFurnace.currentProcessTime = par2;
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
    
        return tileFurnace.isUsableByPlayer(entityplayer);
    }
    
}
