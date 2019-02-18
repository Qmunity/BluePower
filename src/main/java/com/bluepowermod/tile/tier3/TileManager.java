/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tile.IFuzzyRetrieving;
import com.bluepowermod.tile.IRejectAnimator;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import java.util.List;

/**
 * @author MineMaarten
 */
public class TileManager extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive, IRejectAnimator, IFuzzyRetrieving {

    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(25, ItemStack.EMPTY);
    public TubeColor filterColor = TubeColor.NONE;
    public int priority;
    public int mode;
    public int fuzzySetting;
    private int rejectTicker = -1;

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, EnumFacing from, boolean simulate) {

        if (from == getFacingDirection().getOpposite()) {
            // if (!isBufferEmpty()) return stack;
            int itemsAccepted = acceptedItems(stack.stack);
            if (itemsAccepted > 0) {
                if (itemsAccepted >= stack.stack.getCount()) {
                    ItemStack rejectedStack = IOHelper.insert(getTileCache(getFacingDirection()), stack.stack, from, simulate);
                    if (rejectedStack.isEmpty() || rejectedStack.getCount() != stack.stack.getCount()) {
                        if (!simulate) {
                            rejectTicker = 0;
                            sendUpdatePacket();
                        }
                    }
                    if (rejectedStack.isEmpty()) {
                        return null;
                    } else {
                        stack.stack = rejectedStack;
                        return stack;
                    }
                }
                TubeStack injectedStack = stack.copy();
                stack.stack.setCount(stack.stack.getCount() - itemsAccepted);

                injectedStack.stack.setCount(itemsAccepted);
                ItemStack rejectedStack = IOHelper.insert(getTileCache(getFacingDirection()), injectedStack.stack, from, simulate);
                if (rejectedStack.isEmpty() || rejectedStack.getCount() != injectedStack.stack.getCount()) {
                    if (!simulate) {
                        rejectTicker = 0;
                        sendUpdatePacket();
                    }
                }
                if (!rejectedStack.isEmpty()) {
                    stack.stack.setCount(stack.stack.getCount() + rejectedStack.getCount());
                }
            }
        }
        stack.setTarget(null, null);
        return super.acceptItemFromTube(stack, from, simulate);
    }

    private int acceptedItems(ItemStack item) {

        if (item.isEmpty())
            return 0;
        int managerCount = IOHelper.getItemCount(item, this, null, fuzzySetting);
        if (mode == 1 && managerCount > 0)
            return item.getCount();
        return managerCount - IOHelper.getItemCount(item, getTileCache(getFacingDirection()), getFacingDirection().getOpposite(), fuzzySetting);
    }

    @Override
    public void update() {

        if (!world.isRemote && getTicker() % BUFFER_EMPTY_INTERVAL == 0 && isBufferEmpty()) {
            dumpUnwantedItems();
            retrieveItemsFromManagers();
            setOutputtingRedstone(mode == 0 && shouldEmitRedstone());
        }
        if (rejectTicker >= 0) {
            if (++rejectTicker > ANIMATION_TIME) {
                rejectTicker = -1;
                markForRenderUpdate();
            }
        }
        super.update();
    }

    private boolean shouldEmitRedstone() {

        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && acceptedItems(stack) > 0)
                return false;
        }
        return true;
    }

    private void retrieveItemsFromManagers() {

        PneumaticTube tube = MultipartCompatibility.getPart(world, pos.offset(getOutputDirection()), PneumaticTube.class);
        if (tube != null) {
            for (ItemStack stack : inventory) {
                int acceptedItems = acceptedItems(stack);
                if (acceptedItems > 0) {
                    ItemStack retrievingStack = stack.copy();
                    retrievingStack.setCount(retrievingStack.getMaxStackSize());
                    if (tube.getLogic().retrieveStack(this, getOutputDirection(), retrievingStack, filterColor))
                        return;
                }
            }
        }
    }

    private void dumpUnwantedItems() {

        TileEntity te = getTileCache(getFacingDirection());
        IInventory inv = IOHelper.getInventoryForTE(te);
        int[] slots = IOHelper.getAccessibleSlotsForInventory(inv, getFacingDirection().getOpposite());
        for (int slot : slots) {
            ItemStack stack = inv.getStackInSlot(slot);
            int acceptedItems = acceptedItems(stack);
            if (acceptedItems < 0) {
                int rejectedItems = -acceptedItems;
                ItemStack rejectingStack = stack.copy();
                rejectingStack.setCount(Math.min(rejectedItems, rejectingStack.getMaxStackSize()));
                rejectingStack = IOHelper.extract(te, getFacingDirection().getOpposite(), rejectingStack, true, false, fuzzySetting);
                if (!rejectingStack.isEmpty()) {
                    this.addItemToOutputBuffer(rejectingStack, filterColor);
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

        for (int i = 0; i < 24; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory.set(i, new ItemStack(tc));
        }
        filterColor = TubeColor.values()[tCompound.getByte("filterColor")];
        mode = tCompound.getByte("mode");
        priority = tCompound.getByte("priority");
        fuzzySetting = tCompound.getByte("fuzzySetting");
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);

        for (int i = 0; i < 24; i++) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory.get(i).writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
        }

        tCompound.setByte("filterColor", (byte) filterColor.ordinal());
        tCompound.setByte("mode", (byte) mode);
        tCompound.setByte("priority", (byte) priority);
        tCompound.setByte("fuzzySetting", (byte) fuzzySetting);
        return tCompound;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound tag) {

        super.writeToPacketNBT(tag);
        tag.setByte("rejectAnimation", (byte) rejectTicker);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound tag) {

        super.readFromPacketNBT(tag);
        rejectTicker = tag.getByte("rejectAnimation");
    }

    @Override
    public int getSizeInventory() {

        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
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
        if (itemStack != ItemStack.EMPTY) {
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
        return BPBlocks.manager.getUnlocalizedName();
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
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {

        return true;
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
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        EnumFacing direction = getFacingDirection();

        if (side == direction || side == direction.getOpposite()) {
            return new int[] {};
        }
        int[] slots = new int[inventory.size()];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
        return slots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0) {
            filterColor = TubeColor.values()[value];
        } else if (messageId == 1) {
            mode = value;
        } else if (messageId == 2) {
            priority = value;
        } else {
            fuzzySetting = value;
        }
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }

    @Override
    public boolean isRejecting() {

        return rejectTicker >= 0;
    }

    @Override
    public int getFuzzySetting() {
        return fuzzySetting;
    }
}
