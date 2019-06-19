/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.init.BPBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.List;
/**
 * @author MineMaarten
 */
public class TileFilter extends TileTransposer implements ISidedInventory, IGuiButtonSensitive {

    public static final int SLOTS = 9;
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public TubeColor filterColor = TubeColor.NONE;
    public int fuzzySetting;

/*    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, EnumFacing from, boolean simulate) {

        if (from == getFacingDirection() && (!isItemAccepted(stack.stack) || !isBufferEmpty()))
            return stack;
        if (!simulate)
            this.addItemToOutputBuffer(stack.stack, filterColor);
        return null;
    }*/

    @Override
    protected boolean isItemAccepted(ItemStack item) {

        boolean everythingNull = true;
        for (ItemStack invStack : inventory) {
            if (!invStack.isEmpty()) {
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
            Direction dir = getOutputDirection().getOpposite();
            TileEntity tile = getTileCache(dir);
            Direction direction = dir.getOpposite();
            boolean everythingNull = true;
            for (ItemStack filterStack : inventory) {
                if (!filterStack.isEmpty()) {
                    everythingNull = false;
                    ItemStack extractedStack = IOHelper.extract(tile, direction, filterStack, true, false, fuzzySetting);
                    if (!extractedStack.isEmpty()) {
                        this.addItemToOutputBuffer(extractedStack, filterColor);
                        break;
                    }
                }
            }
            if (everythingNull) {
                ItemStack extractedStack = IOHelper.extract(tile, direction, false);
                if (!extractedStack.isEmpty()) {
                    this.addItemToOutputBuffer(extractedStack, filterColor);
                }
            }
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void read(CompoundNBT tCompound) {

        super.read(tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, new ItemStack((IItemProvider) tc));
        }
        filterColor = TubeColor.values()[tCompound.getByte("filterColor")];
        fuzzySetting = tCompound.getByte("fuzzySetting");
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT write(CompoundNBT tCompound) {

        super.write(tCompound);

        for (int i = 0; i < 9; i++) {
                CompoundNBT tc = new CompoundNBT();
                inventory.get(i).write(tc);
                tCompound.put("inventory" + i, tc);
        }

        tCompound.putByte("filterColor", (byte) filterColor.ordinal());
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);

        return tCompound;
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
        return player.getPosition().withinDistance(pos, 64.0D);
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {

        return true;
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
    public int[] getSlotsForFace(Direction side) {
        Direction direction = getFacingDirection();

        if (side == direction || side == direction.getOpposite()) {
            return new int[] {};
        }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }


   /* @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            filterColor = TubeColor.values()[value];
        if (messageId == 1)
            fuzzySetting = value;
    }
*/
    @Override
    public boolean canConnectRedstone() {

        return true;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void onButtonPress(PlayerEntity player, int messageId, int value) {
        if (messageId == 0)
            filterColor = TubeColor.values()[value];
        if (messageId == 1)
            fuzzySetting = value;
    }
}
