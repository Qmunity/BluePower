package com.bluepowermod.container.inventory;

import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageCraftingSync;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *@author MoreThanHidden
 **/
public class InventoryProjectTableCrafting extends InventoryCrafting{

    private final int length;
    private final TileProjectTable projectTable;
    private final Container eventHandler;

    public InventoryProjectTableCrafting(Container eventHandlerIn, TileProjectTable projectTable, int width, int height) {
        super(eventHandlerIn, width, height);
        this.length = width * height;
        this.projectTable = projectTable;
        this.eventHandler = eventHandlerIn;
    }

    @Override
    public boolean isEmpty() {
        return projectTable.isEmpty();
    }

    @Override
    public int getSizeInventory() {
        return this.length;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.projectTable.setCraftingSlotContents(slot, stack);
        eventHandler.onCraftMatrixChanged(this);

    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void markDirty() {
        this.projectTable.markDirty();
        this.eventHandler.onCraftMatrixChanged(this);

        //BPNetworkHandler.INSTANCE.sendToServer(new MessageCraftingSync());
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.projectTable.getStackInCraftingSlot(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(!this.getStackInSlot(index).isEmpty()) {
            ItemStack itemstack;

            if(this.getStackInSlot(index).getCount() <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, ItemStack.EMPTY);
                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
            else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if(this.getStackInSlot(index).getCount() == 0) {
                    this.setInventorySlotContents(index, ItemStack.EMPTY);
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}