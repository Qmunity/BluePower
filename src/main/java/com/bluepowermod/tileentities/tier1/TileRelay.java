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
 *
 *     @author Quetzi
 */

package com.bluepowermod.tileentities.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import com.bluepowermod.tileentities.TileMachineBase;

import java.util.List;

public class TileRelay extends TileMachineBase implements IInventory {

    private final ItemStack[] inventory = new ItemStack[9];


    @Override
    public void updateEntity() {

        super.updateEntity();

        for (int i=0; i < inventory.length; i++) {
            if (inventory[i] != null && inventory[i].stackSize > 0) {
                addItemToOutputBuffer(inventory[i]);
                inventory[i] = null;
                break;
            }
        }
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);

        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);

        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override public int getSizeInventory() {

        return inventory.length;
    }

    /**
     * Returns the stack in slot i
     *
     * @param slot
     */
    @Override public ItemStack getStackInSlot(int slot) {

        return inventory[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     *
     * @param slot
     * @param amount
     */
    @Override public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     *
     * @param slot
     */
    @Override public ItemStack getStackInSlotOnClosing(int slot) {

        return getStackInSlot(slot);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param slot
     * @param itemStack
     */
    @Override public void setInventorySlotContents(int slot, ItemStack itemStack) {

        inventory[slot] = itemStack;
    }

    /**
     * Returns the name of the inventory
     */
    @Override public String getInventoryName() {

        return "tile.relay.name";
    }

    /**
     * Returns if the inventory is named
     */
    @Override public boolean hasCustomInventoryName() {

        return true;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override public int getInventoryStackLimit() {

        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     *
     * @param var1
     */
    @Override public boolean isUseableByPlayer(EntityPlayer var1) {

        return true;
    }

    @Override public void openInventory() {

    }

    @Override public void closeInventory() {

    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     *
     * @param var1
     * @param var2
     */
    @Override public boolean isItemValidForSlot(int var1, ItemStack var2) {

        return true;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null) drops.add(stack);
        return drops;
    }
}
