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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileBuffer extends TileBase implements ISidedInventory {

    private final ItemStack[] allInventories = new ItemStack[20];
    private ItemStack[]       side1Inventory;
    private ItemStack[]       side2Inventory;
    private ItemStack[]       side3Inventory;
    private ItemStack[]       side4Inventory;
    private ItemStack[]       side5Inventory;

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);

        for (int i = 0; i < 20; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            allInventories[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);

        for (int i = 0; i < 20; i++) {
            if (allInventories[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                allInventories[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }

    @Override
    public int getSizeInventory() {

        // This should return 20 for the front face and 4 for all other sides
        return allInventories.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        // this should return the correct slots for the side accessed
        return this.allInventories[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        // this needs to be side aware as well
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

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {

        return getStackInSlot(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

        this.allInventories[i] = itemStack;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String getInventoryName() {

        return "tile.buffer.name";
    }

    @Override
    public boolean hasCustomInventoryName() {

        return true;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {

        return true;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (stack != null) drops.add(stack);
        return drops;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {

        if (ForgeDirection.getOrientation(var1) == ForgeDirection.DOWN) {
            return new int[] { 0, 1, 2, 3 };
        } else if (ForgeDirection.getOrientation(var1) == ForgeDirection.NORTH) {
            return new int[] { 4, 5, 6, 7 };
        } else if (ForgeDirection.getOrientation(var1) == ForgeDirection.SOUTH) {
            return new int[] { 8, 9, 10, 11 };
        } else if (ForgeDirection.getOrientation(var1) == ForgeDirection.EAST) {
            return new int[] { 12, 13, 14, 15 };
        } else if (ForgeDirection.getOrientation(var1) == ForgeDirection.WEST) { 
            return new int[] { 16, 17, 18, 19 }; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {

        for (int i : this.getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {

        for (int i : this.getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
}
