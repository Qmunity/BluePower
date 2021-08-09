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

import com.bluepowermod.client.gui.BPMenuType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceBlockEntity;
import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.util.IntArray;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MineMaarten
 */
public class ContainerAlloyFurnace extends AbstractContainerMenu {
    private ContainerData fields;
    public final Container inventory;

    public ContainerAlloyFurnace(int windowId, Inventory invPlayer, Container inventory, ContainerData fields) {
        super(BPMenuType.ALLOY_FURNACE, windowId);
        this.inventory = inventory;
        this.fields = fields;

        addSlot(new SlotMachineInput(inventory, 0, 21, 35));
        addSlot(new SlotMachineOutput(inventory, 1, 134, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotMachineInput(inventory, i * 3 + j + 2, 47 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        this.addDataSlots(fields);
    }

    public ContainerAlloyFurnace( int id, Inventory invPlayer )    {
        this( id, invPlayer, new Inventory( TileAlloyFurnace.SLOTS ), new IntArray(3));
    }

    protected void bindPlayerInventory(Inventory invPlayer) {
    
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
    public ItemStack quickMoveStack(Player player, int slotIndex) {
    
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();
            
            if (slotIndex < 11) {
                if (!moveItemStackTo(slotItem, 12, 47, false)) return ItemStack.EMPTY;
                slot.onQuickCraft(slotItem, itemStack);
            } else {
                if (FurnaceBlockEntity.isFuel(slotItem) && moveItemStackTo(slotItem, 0, 1, false)) {
                    
                } else if (!moveItemStackTo(slotItem, 2, 11, false)) return ItemStack.EMPTY;
                slot.onQuickCraft(slotItem, itemStack);
            }
            
            if (slotItem.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotItem.getCount() == itemStack.getCount()) return ItemStack.EMPTY;
            
            slot.onQuickCraft(itemStack, slotItem);
        }
        
        return itemStack;
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return inventory.stillValid( playerEntity );
    }

    @OnlyIn(Dist.CLIENT)
    public float getBurningPercentage() {

        if (fields.get(2) > 0) {
            return (float) fields.get(0) / (float) fields.get(2);
        } else {
            return 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getProcessPercentage() {

        return (float) fields.get(1) / 200;
    }



}
