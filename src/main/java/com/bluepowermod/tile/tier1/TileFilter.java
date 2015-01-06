/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.tube.TubeStack;

/**
 * @author MineMaarten
 */
public class TileFilter extends TileTransposer implements ISidedInventory, IGuiButtonSensitive {

    protected final ItemStack[] inventory = new ItemStack[9];
    public TubeColor filterColor = TubeColor.NONE;
    public int fuzzySetting;

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {

        if (from == getFacingDirection() && (!isItemAccepted(stack.stack) || !isBufferEmpty()))
            return stack;
        if (!simulate)
            this.addItemToOutputBuffer(stack.stack, filterColor);
        return null;
    }

    @Override
    protected boolean isItemAccepted(ItemStack item) {

        boolean everythingNull = true;
        for (ItemStack invStack : inventory) {
            if (invStack != null) {
                if (ItemStackHelper.areStacksEqual(invStack, item, fuzzySetting)) {
                    return true;
                }
                everythingNull = false;
            }
        }
        return everythingNull;
    }

    @Override
    protected TubeColor getAcceptedItemColor(ItemStack item) {

        return filterColor;
    }

    @Override
    protected void pullItem() {

        if (isBufferEmpty()) {
            ForgeDirection dir = getOutputDirection().getOpposite();
            TileEntity tile = getTileCache(dir);
            ForgeDirection direction = dir.getOpposite();
            boolean everythingNull = true;
            for (ItemStack filterStack : inventory) {
                if (filterStack != null) {
                    everythingNull = false;
                    ItemStack extractedStack = IOHelper.extract(tile, direction, filterStack, true, false, fuzzySetting);
                    if (extractedStack != null) {
                        this.addItemToOutputBuffer(extractedStack, filterColor);
                        break;
                    }
                }
            }
            if (everythingNull) {
                ItemStack extractedStack = IOHelper.extract(tile, direction, false);
                if (extractedStack != null) {
                    this.addItemToOutputBuffer(extractedStack, filterColor);
                }
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
        filterColor = TubeColor.values()[tCompound.getByte("filterColor")];
        fuzzySetting = tCompound.getByte("fuzzySetting");
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

        tCompound.setByte("filterColor", (byte) filterColor.ordinal());
        tCompound.setByte("fuzzySetting", (byte) fuzzySetting);
    }

    @Override
    public int getSizeInventory() {

        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

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

        ItemStack itemStack = getStackInSlot(i);
        if (itemStack != null) {
            setInventorySlotContents(i, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

        inventory[i] = itemStack;
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.filter.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
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
        for (ItemStack stack : inventory)
            if (stack != null)
                drops.add(stack);
        return drops;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {

        ForgeDirection direction = getFacingDirection();

        if (var1 == direction.ordinal() || var1 == direction.getOpposite().ordinal()) {
            return new int[] {};
        }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {

        return true;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {

        return true;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            filterColor = TubeColor.values()[value];
        if (messageId == 1)
            fuzzySetting = value;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }
}
