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
 *     @author Lumien
 */

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.container.slot.SlotExclude;
import com.bluepowermod.container.slot.SlotLocked;
import com.bluepowermod.init.BPItems;
//import invtweaks.api.container.ChestContainer;
//import invtweaks.api.container.ContainerSection;
//import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

//@ChestContainer
public class ContainerCanvasBag extends Container {
    
    IInventory canvasBagInventory;

    public ContainerCanvasBag(int windowId, PlayerInventory playerInventory, IInventory inventory) {
    super(BPContainerType.CANVAS_BAG, windowId);
        this.canvasBagInventory = inventory;
        int i = -1 * 18;
        //canvasBagInventory.openInventory();
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlot(new SlotExclude(canvasBagInventory, k + j * 9, 8 + k * 18, 18 + j * 18, BPItems.canvas_bag));
            }
        }
        
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        
        for (int j = 0; j < 9; ++j) {
            if (playerInventory.getStackInSlot(j) == ((InventoryItem) canvasBagInventory).getItem()) {
                addSlot(new SlotLocked(playerInventory, j, 8 + j * 18, 161 + i));
            } else {
                addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
            }
            addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
        }
        
        this.canvasBagInventory = canvasBagInventory;
    }

    public ContainerCanvasBag( int id, PlayerInventory player )    {
        this( id, player, new Inventory( 9));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
    
        return canvasBagInventory.isUsableByPlayer(player);
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, ClickType par3, PlayerEntity player) {
    
        if (par3.ordinal() != 2 || player.inventory.currentItem != par2) {
            return super.slotClick(par1, par2, par3, player);
        } else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2) {
    
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(par2);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (par2 < 27) {
                if (!mergeItemStack(itemstack1, 27, 63, true)) { return ItemStack.EMPTY; }
            } else if (!mergeItemStack(itemstack1, 0, 27, false)) { return ItemStack.EMPTY; }
            
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            
            if (itemstack1.getCount() == itemstack.getCount()) { return ItemStack.EMPTY; }
            
            slot.onSlotChange(itemstack, itemstack1);
        }
        
        return itemstack;
    }
    
    @Override
    public boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
    
        boolean flag1 = false;
        int k = par2;
        
        if (par4) {
            k = par3 - 1;
        }
        
        Slot slot;
        ItemStack itemstack1;
        
        if (par1ItemStack.isStackable()) {
            while (par1ItemStack.getCount() > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
                slot = (Slot) inventorySlots.get(k);
                itemstack1 = slot.getStack();
                
                if (!itemstack1.isEmpty() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.hasContainerItem() || par1ItemStack.getDamage() == itemstack1.getDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack)) {
                    int l = itemstack1.getCount() + par1ItemStack.getCount();
                    
                    if (l <= par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.setCount(0);
                        itemstack1.setCount(l);
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.setCount(par1ItemStack.getCount() - par1ItemStack.getMaxStackSize() - itemstack1.getCount());
                        itemstack1.setCount(par1ItemStack.getMaxStackSize());
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                
                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        
        if (par1ItemStack.getCount() > 0) {
            if (par4) {
                k = par3 - 1;
            } else {
                k = par2;
            }
            
            while (!par4 && k < par3 || par4 && k >= par2) {
                slot = (Slot) inventorySlots.get(k);
                itemstack1 = slot.getStack();
                
                if (itemstack1.isEmpty() && slot.isItemValid(par1ItemStack)) {
                    if (1 < par1ItemStack.getCount()) {
                        ItemStack copy = par1ItemStack.copy();
                        copy.setCount(1);
                        slot.putStack(copy);

                        par1ItemStack.setCount(par1ItemStack.getCount() - 1);
                        flag1 = true;
                        break;
                    } else {
                        slot.putStack(par1ItemStack.copy());
                        slot.onSlotChanged();
                        par1ItemStack.setCount(0);
                        flag1 = true;
                        break;
                    }
                }
                
                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return flag1;
    }

    /*TODO: Inventory Tweaks Support find @Optional.Method alternative
    @Optional.Method(modid = Dependencies.INVTWEAKS)
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSections() {

        Map<ContainerSection, List<Slot>> sections = new HashMap<ContainerSection, List<Slot>>();
        List<Slot> slotsChest = new ArrayList<Slot>();
        List<Slot> slotsInventory = new ArrayList<Slot>();
        for (int i = 0; i < 27; i++) {
            slotsChest.add(i, (Slot) inventorySlots.get(i));
        }
        for (int i = 0; i < 36; i++) {
            slotsInventory.add(0, (Slot) inventorySlots.get(i + 27));
        }
        sections.put(ContainerSection.CHEST, slotsChest);
        sections.put(ContainerSection.INVENTORY, slotsInventory);
        return sections;
    }*/
}
