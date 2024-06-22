package com.bluepowermod.container.inventory;

import com.bluepowermod.network.message.MessageCraftingSync;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *@author MoreThanHidden
 **/
public class InventoryProjectTableCrafting extends TransientCraftingContainer {

    private final int length;
    private final Container projectTable;
    private final AbstractContainerMenu eventHandler;

    public InventoryProjectTableCrafting(AbstractContainerMenu eventHandlerIn, Container projectTable, int width, int height) {
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
            PacketDistributor.sendToServer(new MessageCraftingSync(0));
    }

    @Nonnull
    @Override
    public ItemStack getItem(int index) {
        return 18 + index >= 18 + this.getContainerSize() ? ItemStack.EMPTY : this.projectTable.getItem(18 + index);
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> list = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < 9; i++) {
                list.set(i, this.projectTable.getItem(18 + i));
        }
        return list;
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