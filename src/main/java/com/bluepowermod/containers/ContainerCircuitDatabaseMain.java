package com.bluepowermod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.client.gui.GuiBase;
import com.bluepowermod.containers.slots.SlotMachineOutput;
import com.bluepowermod.containers.slots.SlotPhantom;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCircuitDatabaseMain extends ContainerGhosts {
    
    private int                       curUploadProgress, curCopyProgress, selectedShareOption;
    private final TileCircuitDatabase circuitDatabase;
    
    public ContainerCircuitDatabaseMain(InventoryPlayer invPlayer, TileCircuitDatabase circuitDatabase) {
    
        this.circuitDatabase = circuitDatabase;
        addSlotToContainer(new SlotPhantom(circuitDatabase.copyInventory, 0, 57, 64));
        addSlotToContainer(new SlotMachineOutput(circuitDatabase.copyInventory, 1, 108, 64));
        
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(circuitDatabase, j + i * 9, 8 + j * 18, 95 + i * 18));
            }
        }
        
        bindPlayerInventory(invPlayer);
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 142 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 200));
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return true;
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
    
        super.detectAndSendChanges();
        
        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;
            
            if (curUploadProgress != circuitDatabase.curUploadProgress) {
                icrafting.sendProgressBarUpdate(this, 0, circuitDatabase.curUploadProgress);
            }
            if (curCopyProgress != circuitDatabase.curCopyProgress) {
                icrafting.sendProgressBarUpdate(this, 1, circuitDatabase.curCopyProgress);
            }
            if (selectedShareOption != circuitDatabase.selectedShareOption) {
                icrafting.sendProgressBarUpdate(this, 2, circuitDatabase.selectedShareOption);
            }
        }
        curUploadProgress = circuitDatabase.curUploadProgress;
        curCopyProgress = circuitDatabase.curCopyProgress;
        selectedShareOption = circuitDatabase.selectedShareOption;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
    
        if (id == 0) {
            circuitDatabase.curUploadProgress = value;
        }
        if (id == 1) {
            circuitDatabase.curCopyProgress = value;
        }
        if (id == 2) {
            circuitDatabase.selectedShareOption = value;
            ((GuiBase) ClientProxy.getOpenedGui()).redraw();
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
    
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!mergeItemStack(itemstack1, 20, 55, false)) return null;
            } else {
                if (!mergeItemStack(itemstack1, 2, 20, false)) return null;
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
