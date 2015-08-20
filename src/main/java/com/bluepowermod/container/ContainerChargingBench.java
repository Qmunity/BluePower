package com.bluepowermod.container;

import com.bluepowermod.tile.tier2.TileChargingBench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.inventory.ContainerBase;

/**
 * @author Koen Beckers (K-4U)
 */
public class ContainerChargingBench extends ContainerBase<TileChargingBench> {

    private final TileChargingBench tileChargingBench;

    public ContainerChargingBench(InventoryPlayer invPlayer, TileChargingBench bench) {

        super(bench);
        tileChargingBench = bench;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 6; ++j) {
                addSlotToContainer(new Slot(bench, j + i * 6, 62 + j * 18, 19 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 162));
        }
    }


    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return tileChargingBench.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 24) {
                if (!mergeItemStack(itemstack1, 24, 60, false))
                    return null;
            } else if (!mergeItemStack(itemstack1, 0, 24, false)) {
                return null;
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
