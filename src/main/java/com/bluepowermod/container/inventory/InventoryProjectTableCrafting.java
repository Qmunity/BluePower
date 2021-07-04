package com.bluepowermod.container.inventory;

import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageCraftingSync;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

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
    public int getContainerSize() {
        return this.length;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.projectTable.setItem(18 + slot, stack);
        eventHandler.slotsChanged(this);
    }

    @Override
    public void setChanged() {
        this.projectTable.setChanged();
        this.eventHandler.slotsChanged(this);
        if(FMLEnvironment.dist == Dist.CLIENT)
            BPNetworkHandler.wrapper.sendToServer(new MessageCraftingSync());
    }

    @Nonnull
    @Override
    public ItemStack getItem(int index) {
        return 18 + index >= 18 + this.getContainerSize() ? ItemStack.EMPTY : this.projectTable.getItem(18 + index);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int index, int count) {
        if(!this.getItem(index).isEmpty()) {
            ItemStack itemstack;

            if(this.getItem(index).getCount() <= count) {
                itemstack = this.getItem(index);
                this.setItem(index, ItemStack.EMPTY);
                this.eventHandler.slotsChanged(this);
                return itemstack;
            }
            else {
                itemstack = this.getItem(index).split(count);

                if(this.getItem(index).getCount() == 0) {
                    this.setItem(index, ItemStack.EMPTY);
                }

                this.eventHandler.slotsChanged(this);
                return itemstack;
            }
        }
        else {
            return ItemStack.EMPTY;
        }
    }
}