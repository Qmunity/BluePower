/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.IGUITextFieldSensitive;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * @author MineMaarten
 */
public class TileCircuitTable extends TileBase implements IInventory, IGUITextFieldSensitive, IGuiButtonSensitive {

    public static final int SLOTS = 24;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public final Inventory circuitInventory = new Inventory( SLOTS);
    public int slotsScrolled;
    private String textboxString = "";

    public TileCircuitTable() {
        super(BPTileEntityType.CIRCUIT_TABLE);
    }

    @Override
    public void setText(int textFieldID, String text) {

        textboxString = text;
    }

    @Override
    public String getText(int textFieldID) {

        return textboxString;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        super.write(tag);

        ListNBT tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).write(tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("Items", tagList);

        tag.putString("textboxString", textboxString);
        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {

        super.read(tag);

        ListNBT tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(24, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, new ItemStack((IItemProvider) tagCompound));
            }
        }

        textboxString = tag.getString("textboxString");
    }

    @Override
    public int getSizeInventory() {

        return inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        ItemStack itemStack = getStackInSlot(i);
        if (!itemStack.isEmpty()) {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getPosition().withinDistance(pos,64.0D);
    }


    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

    @Override
    public void onButtonPress(PlayerEntity player, int messageId, int value) {
        if (messageId == 0) {
            slotsScrolled = value;
        }
        //updateGateInventory();
        //((ICrafting) player).sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
    }
}
