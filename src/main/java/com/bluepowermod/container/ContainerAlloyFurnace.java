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

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;

/**
 * @author MineMaarten
 */
public class ContainerAlloyFurnace extends Container {
    private int currentBurnTime;
    private int maxBurnTime;
    public int currentProcessTime;

    private final IInventory inventory;

    public ContainerAlloyFurnace(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.ALLOY_FURNACE, windowId);
        this.inventory = inventory;

        addSlot(new SlotMachineInput(inventory, 0, 21, 35));
        addSlot(new SlotMachineOutput(inventory, 1, 134, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotMachineInput(inventory, i * 3 + j + 2, 47 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerAlloyFurnace( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileAlloyFurnace.SLOTS ));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2) {
    
        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = inventorySlots.get(par2);
        
        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            
            if (par2 < 11) {
                if (!mergeItemStack(var5, 11, 47, false)) return ItemStack.EMPTY;
                var4.onSlotChange(var5, var3);
            } else {
                if (FurnaceTileEntity.isFuel(var5) && mergeItemStack(var5, 0, 1, false)) {
                    
                } else if (!mergeItemStack(var5, 2, 11, false)) return ItemStack.EMPTY;
                var4.onSlotChange(var5, var3);
            }
            
            if (var5.getCount() == 0) {
                var4.putStack(ItemStack.EMPTY);
            } else {
                var4.onSlotChanged();
            }
            
            if (var5.getCount() == var3.getCount()) return ItemStack.EMPTY;
            
            var4.onSlotChange(var3, var5);
        }
        
        return var3;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return inventory.isUsableByPlayer( playerEntity );
    }

}
