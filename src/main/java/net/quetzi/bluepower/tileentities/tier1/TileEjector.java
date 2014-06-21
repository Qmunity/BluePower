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

package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileEjector extends TileBase implements IInventory {

    private final ItemStack[] inventory = new ItemStack[9];

    /**
     * Returns the number of slots in the inventory.
     */
    @Override public int getSizeInventory() {

        return 0;
    }

    /**
     * Returns the stack in slot i
     *
     * @param var1
     */
    @Override public ItemStack getStackInSlot(int var1) {

        return null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     *
     * @param var1
     * @param var2
     */
    @Override public ItemStack decrStackSize(int var1, int var2) {

        return null;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     *
     * @param var1
     */
    @Override public ItemStack getStackInSlotOnClosing(int var1) {

        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param var1
     * @param var2
     */
    @Override public void setInventorySlotContents(int var1, ItemStack var2) {

    }

    /**
     * Returns the name of the inventory
     */
    @Override public String getInventoryName() {

        return null;
    }

    /**
     * Returns if the inventory is named
     */
    @Override public boolean hasCustomInventoryName() {

        return false;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override public int getInventoryStackLimit() {

        return 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     *
     * @param var1
     */
    @Override public boolean isUseableByPlayer(EntityPlayer var1) {

        return false;
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

        return false;
    }
}
