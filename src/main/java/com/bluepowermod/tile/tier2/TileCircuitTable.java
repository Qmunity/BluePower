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
import com.bluepowermod.tile.IGUITextFieldSensitive;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MineMaarten
 */
public class TileCircuitTable extends TileBase implements IInventory, IGUITextFieldSensitive, IGuiButtonSensitive {

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(24, ItemStack.EMPTY);
    public final InventoryBasic circuitInventory = new InventoryBasic("circuitInventory", false, 24);
    public int slotsScrolled;
    private String textboxString = "";

    public TileCircuitTable() {
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
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
        }
        tag.setTag("Items", tagList);

        tag.setString("textboxString", textboxString);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = NonNullList.withSize(24, ItemStack.EMPTY);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, new ItemStack(tagCompound));
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
                itemStack = itemStack.splitStack(amount);
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
    public String getName() {

        return BPBlocks.circuit_table.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {

        return true;
    }


    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
        if (messageId == 0) {
            slotsScrolled = value;
        }
        //updateGateInventory();
        //((ICrafting) player).sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
    }
}
