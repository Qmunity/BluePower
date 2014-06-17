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
 */

package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.tileentities.tier1.TileBuffer;

public class ContainerBuffer extends Container {

    private final TileBuffer tileBuffer;

    public ContainerBuffer(InventoryPlayer invPlayer, TileBuffer buffer) {

        tileBuffer = buffer;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlotToContainer(new Slot(buffer, i * 5 + j, 44 + j * 18, 9 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }

    @Override public boolean canInteractWith(EntityPlayer player) {

        return tileBuffer.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {

        ItemStack var3 = null;
        Slot var4 = (Slot) inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < 20) {
                if (!mergeItemStack(var5, 11, 47, false)) return null;
                var4.onSlotChange(var5, var3);
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            } else {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize) return null;

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
}
