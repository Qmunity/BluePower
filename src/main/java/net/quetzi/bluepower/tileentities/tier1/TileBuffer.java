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
import net.quetzi.bluepower.tileentities.TileMachineBase;

public class TileBuffer extends TileMachineBase implements IInventory {

    private ItemStack[] allInventories;
    private ItemStack[] side1Inventory;
    private ItemStack[] side2Inventory;
    private ItemStack[] side3Inventory;
    private ItemStack[] side4Inventory;
    private ItemStack[] side5Inventory;


    private ItemStack[] getInventoryForSide(int i) {
        if ((i >= 0) && (i < 4)) {
            for (int j=0; j<4;j++) {
                side1Inventory[j] = allInventories[i];
            }
            return side1Inventory;
        } else if ((i > 3) && (i < 8)) {
            for (int j=0; j<4;j++) {
                side2Inventory[j] = allInventories[i+4];
            }
            return side2Inventory;
        } else if ((i > 7) && (i < 12)) {
            for (int j=0; j<4;j++) {
                side3Inventory[j] = allInventories[i+8];
            }
            return side3Inventory;
        } else if ((i > 11) && (i < 16)) {
            for (int j=0; j<4;j++) {
                side4Inventory[j] = allInventories[i+12];
            }
            return side4Inventory;
        } else if ((i > 15) && (i < 20)) {
            for (int j=0; j<4;j++) {
                side5Inventory[j] = allInventories[i+16];
            }
            return side5Inventory;
        }
        return allInventories;
    }


    @Override
    public int getSizeInventory() {

        return 20;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        return allInventories[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int i2) {

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {

        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

    }

    @Override
    public String getInventoryName() {

        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {

        return false;
    }
}
