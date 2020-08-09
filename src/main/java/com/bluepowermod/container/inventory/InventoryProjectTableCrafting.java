package com.bluepowermod.container.inventory;

import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageCraftingSync;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *@author MoreThanHidden
 **/
public class InventoryProjectTableCrafting extends CraftingInventory {

    private final int length;
    private final IInventory projectTable;
    private final Container eventHandler;

    public InventoryProjectTableCrafting(Container eventHandlerIn, IInventory projectTable, int width, int height) {
        super(eventHandlerIn, width, height);
        this.length = width * height;
        this.projectTable = projectTable;
        this.eventHandler = eventHandlerIn;
    }

    @Override
    public int getSizeInventory() {
        return this.length;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.projectTable.setInventorySlotContents(18 + slot, stack);
        eventHandler.onCraftMatrixChanged(this);

    }

    @Override
    public void markDirty() {
        this.projectTable.markDirty();
        this.eventHandler.onCraftMatrixChanged(this);
        BPNetworkHandler.wrapper.sendToServer(new MessageCraftingSync());
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return 18 + index >= 18 + this.getSizeInventory() ? ItemStack.EMPTY : this.projectTable.getStackInSlot(18 + index);
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
                itemstack = this.getStackInSlot(index).split(count);

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