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

import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.container.slot.SlotExclude;
import com.bluepowermod.container.slot.SlotLocked;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.util.Dependencies;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChestContainer
public class ContainerCanvasBag extends Container {
    
    IInventory canvasBagInventory;
    ItemStack  bag;

    public ContainerCanvasBag(ItemStack bag, IInventory playerInventory, IInventory canvasBagInventory) {
    
        this.bag = bag;
        int i = -1 * 18;
        //canvasBagInventory.openInventory();
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlotToContainer(new SlotExclude(canvasBagInventory, k + j * 9, 8 + k * 18, 18 + j * 18, BPItems.canvas_bag));
            }
        }
        
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        
        for (int j = 0; j < 9; ++j) {
            if (playerInventory.getStackInSlot(j) == ((InventoryItem) canvasBagInventory).getItem()) {
                addSlotToContainer(new SlotLocked(playerInventory, j, 8 + j * 18, 161 + i));
            } else {
                addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
            }
            addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
        }
        
        this.canvasBagInventory = canvasBagInventory;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), bag);
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, ClickType par3, EntityPlayer player) {
    
        if (par3.ordinal() != 2 || player.inventory.currentItem != par2) {
            return super.slotClick(par1, par2, par3, player);
        } else {
            return null;
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (par2 < 27) {
                if (!mergeItemStack(itemstack1, 27, 63, true)) { return null; }
            } else if (!mergeItemStack(itemstack1, 0, 27, false)) { return null; }
            
            if (itemstack1.getCount() == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
            
            if (itemstack1.getCount() == itemstack.getCount()) { return null; }
            
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
                
                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack)) {
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
                
                if (itemstack1 == null && slot.isItemValid(par1ItemStack)) {
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
    }
}
