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
import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceBlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MoreThanHidden
 */
public class ContainerBlulectricAlloyFurnace extends AbstractContainerMenu {
    private ContainerData fields;
    public final Container inventory;

    public ContainerBlulectricAlloyFurnace( int id, Inventory player ){
        this( id, player, new SimpleContainer(TileBlulectricAlloyFurnace.SLOTS), new SimpleContainerData(3));
    }

    public ContainerBlulectricAlloyFurnace(int windowId, Inventory invPlayer, Container inventory, ContainerData fields) {
        super(BPContainerType.BLULECTRIC_ALLOY_FURNACE, windowId);
        this.inventory = inventory;
        this.fields = fields;

        addSlot(new SlotMachineOutput(inventory, 0, 141, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotMachineInput(inventory, i * 3 + j + 1, 48 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
        this.addDataSlots(fields);
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
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2) {

        ItemStack var3 = ItemStack.EMPTY;
        Slot var4 = slots.get(par2);

        if (var4 != null && var4.hasItem()) {
            ItemStack var5 = var4.getItem();
            var3 = var5.copy();

            if (par2 < 10) {
                if (!moveItemStackTo(var5, 11, 46, false)) return ItemStack.EMPTY;
                var4.onQuickCraft(var5, var3);
            } else {
                if (FurnaceBlockEntity.isFuel(var5) && moveItemStackTo(var5, 0, 1, false)) {

                } else if (!moveItemStackTo(var5, 1, 10, false)) return ItemStack.EMPTY;
                var4.onQuickCraft(var5, var3);
            }

            if (var5.getCount() == 0) {
                var4.set(ItemStack.EMPTY);
            } else {
                var4.setChanged();
            }

            if (var5.getCount() == var3.getCount()) return ItemStack.EMPTY;

            var4.onQuickCraft(var3, var5);
        }

        return var3;
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
