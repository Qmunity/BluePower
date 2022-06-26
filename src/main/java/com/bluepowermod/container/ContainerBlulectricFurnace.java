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
import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MoreThanHidden
 */
public class ContainerBlulectricFurnace extends AbstractContainerMenu {
    private ContainerData fields;
    public final Container inventory;

    public ContainerBlulectricFurnace( int id, Inventory player ){
        this( id, player, new SimpleContainer(TileBlulectricFurnace.SLOTS), new SimpleContainerData(3));
    }

    public ContainerBlulectricFurnace(int windowId, Inventory invPlayer, Container inventory, ContainerData fields) {
        super(BPMenuType.BLULECTRIC_FURNACE.get(), windowId);
        this.inventory = inventory;
        this.fields = fields;

        addSlot(new SlotMachineInput(inventory, 0, 62, 35));
        addSlot(new SlotMachineOutput(inventory, 1, 126, 35));

        bindPlayerInventory(invPlayer);
        this.addDataSlots(fields);
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
    public ItemStack quickMoveStack(Player playerIn, int index) {

        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            stack1 = stack.copy();

            if (index < 2) {
                if (!moveItemStackTo(stack, 2, 37, false))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(stack, stack1);
            } else {
                if (!moveItemStackTo(stack, 0, 1, false))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(stack, stack1);
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == stack1.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, stack);
        }

        return stack1;
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return inventory.stillValid( playerEntity );
    }

    //fields.get(2) = Max | fields.get(0) = Amount
    @OnlyIn(Dist.CLIENT)
    public float getBufferPercentage() {

        if (fields.get(2) > 0) {
            return (float) fields.get(0) / (float) fields.get(2);
        } else {
            return 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getProcessPercentage() {
        return (float) fields.get(1) / (100 / (fields.get(0) / (float) fields.get(2)));
    }

}
