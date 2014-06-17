package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.containers.inventorys.InventoryItem;
import net.quetzi.bluepower.containers.slots.SlotLocked;
import net.quetzi.bluepower.items.ItemCanvasBag;

public class ContainerCanvasBag extends Container {
    
    IInventory canvasBagInventory;
    
    public ContainerCanvasBag(IInventory playerInventory, IInventory canvasBagInventory) {
    
        int i = -1 * 18;
        canvasBagInventory.openInventory();
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(canvasBagInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        
        for (int j = 0; j < 9; ++j) {
            if (playerInventory.getStackInSlot(i) == ((InventoryItem) canvasBagInventory).getItem()) {
                this.addSlotToContainer(new SlotLocked(playerInventory, j, 8 + j * 18, 161+i));
            } else {
                this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 161+i));
            }
            this.addSlotToContainer(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
        }
        
        this.canvasBagInventory = canvasBagInventory;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemCanvasBag;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (par2 < 27) {
                if (!this.mergeItemStack(itemstack1, 27, 63, true)) { return null; }
            } else if (!this.mergeItemStack(itemstack1, 0, 27, false)) { return null; }
            
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
            
            if (itemstack1.stackSize == itemstack.stackSize) { return null; }
            
            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
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
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();
                
                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem()
                        && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage())
                        && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack)) {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;
                    
                    if (l <= par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
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
        
        if (par1ItemStack.stackSize > 0) {
            if (par4) {
                k = par3 - 1;
            } else {
                k = par2;
            }
            
            while (!par4 && k < par3 || par4 && k >= par2) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();
                
                if (itemstack1 == null && slot.isItemValid(par1ItemStack)) {
                    if (1 < par1ItemStack.stackSize) {
                        ItemStack copy = par1ItemStack.copy();
                        copy.stackSize = 1;
                        slot.putStack(copy);
                        
                        par1ItemStack.stackSize -= 1;
                        flag1 = true;
                        break;
                    } else {
                        slot.putStack(par1ItemStack.copy());
                        slot.onSlotChanged();
                        par1ItemStack.stackSize = 0;
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
}
