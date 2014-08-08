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
 *
 *     @author Quetzi
 */

package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.containers.slots.SlotProjectTableCrafting;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.tier1.TileProjectTable;

/**
 * @author MineMaarten
 */
public class ContainerProjectTable extends Container {
    
    private final TileProjectTable  projectTable;
    private final InventoryCrafting craftingGrid;
    private int                     itemsCrafted;
    private boolean                 isRetrying = false;
    
    public ContainerProjectTable(InventoryPlayer invPlayer, TileProjectTable projectTable) {
    
        this.projectTable = projectTable;
        craftingGrid = projectTable.getCraftingGrid(this);
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                //When changing the 34 and 16, this will break the NEI shift clicking the question mark. See NEIPluginInitConfig
                addSlotToContainer(new Slot(craftingGrid, j + i * 3, 34 + j * 18, 16 + i * 18));
            }
        }
        
        addSlotToContainer(new SlotProjectTableCrafting(projectTable, invPlayer.player, craftingGrid, projectTable.craftResult, 0, 127, 34));
        
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(projectTable, j + i * 9, 8 + j * 18, 79 + i * 18));
            }
        }
        
        bindPlayerInventory(invPlayer);
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 184));
        }
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
    
        if (craftingGrid != null) projectTable.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftingGrid, projectTable.getWorldObj()));
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return projectTable.isUseableByPlayer(player);
    }
    
    @Override
    protected void retrySlotClick(int slot, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
    
        ItemStack stackInSlot = ((Slot) inventorySlots.get(slot)).getStack();
        itemsCrafted += stackInSlot.stackSize;
        isRetrying = true;
        if (slot != 9 || !isLastCraftingOperation() && itemsCrafted < stackInSlot.getMaxStackSize()) {
            slotClick(slot, p_75133_2_, 1, p_75133_4_);//only crafting slot doesn't retry clicking so no more than 64 items get crafted at a time
        }
        isRetrying = false;
    }
    
    private boolean isLastCraftingOperation() {
    
        for (int i = 0; i < 9; i++) {
            ItemStack stack = craftingGrid.getStackInSlot(i);
            if (stack != null && stack.stackSize == 1 && extractStackFromTable(projectTable, stack, true) == null && (!stack.getItem().hasContainerItem(stack) || stack.getItem().doesContainerItemLeaveCraftingGrid(stack))) return true;
        }
        return false;
    }
    
    public static ItemStack extractStackFromTable(TileProjectTable table, ItemStack stack, boolean simulate) {
    
        return IOHelper.extract(table, ForgeDirection.UNKNOWN, stack, true, simulate);
    }
    
    /*
     * 0-8 matrix, 9 result, 10 - 27 inventory, 28 - 63 player inv.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
    
        if (!isRetrying) itemsCrafted = 0;
        
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 9) {
                if (!mergeItemStack(itemstack1, 10, 28, false)) return null;
            } else if (par2 == 9) {
                if (!mergeItemStack(itemstack1, 28, 64, false)) return null;
            } else if (par2 < 28) {
                if (!mergeItemStack(itemstack1, 28, 64, false)) return null;
            } else {
                if (!mergeItemStack(itemstack1, 10, 28, false)) return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize) {
                slot.onPickupFromSlot(player, itemstack1);
            } else {
                return null;
            }
        }
        return itemstack;
    }
}
