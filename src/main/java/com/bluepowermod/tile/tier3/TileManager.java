/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.IFuzzyRetrieving;
import com.bluepowermod.tile.IRejectAnimator;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3i;

/**
 * @author MineMaarten
 */
public class TileManager extends TileMachineBase implements ISidedInventory,  IRejectAnimator, IFuzzyRetrieving, IGuiButtonSensitive {
    public static final int SLOTS = 25;
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public TubeColor filterColor = TubeColor.NONE;
    public int priority;
    public int mode;
    public int fuzzySetting;
    private int rejectTicker = -1;

    public TileManager() {
        super(BPTileEntityType.MANAGER);
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
    public void onButtonPress(PlayerEntity player, int messageId, int value) {

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
    public void tick() {

        if (!world.isRemote && getTicker() % BUFFER_EMPTY_INTERVAL == 0) {
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
        super.tick();
    }

    private boolean shouldEmitRedstone() {

        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && acceptedItems(stack) > 0)
                return false;
        }
        return true;
    }

    private void retrieveItemsFromManagers() {
        //Optional<IMultipartContainer> container = MultipartHelper.getContainer(world, pos.offset(getOutputDirection()));
        //if (container.isPresent()) {
        //}
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
    public void read(BlockState blockState, CompoundNBT tCompound) {

        super.read(blockState, tCompound);

        for (int i = 0; i < 24; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.read(tc));
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
    public CompoundNBT write(CompoundNBT tCompound) {

        super.write(tCompound);

        for (int i = 0; i < 24; i++) {
                CompoundNBT tc = new CompoundNBT();
                inventory.get(i).write(tc);
                tCompound.put("inventory" + i, tc);
        }

        tCompound.putByte("filterColor", (byte) filterColor.ordinal());
        tCompound.putByte("mode", (byte) mode);
        tCompound.putByte("priority", (byte) priority);
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);
        return tCompound;
    }

    @Override
    public void writeToPacketNBT(CompoundNBT tag) {

        super.writeToPacketNBT(tag);
        tag.putByte("rejectAnimation", (byte) rejectTicker);
    }

    @Override
    public void readFromPacketNBT(CompoundNBT tag) {

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
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return pos.withinDistance(new Vector3i(player.serverPosX, player.serverPosY, player.serverPosZ), 64.0D);
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
    public void clear() {

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
    public int[] getSlotsForFace(Direction side) {
        Direction direction = getFacingDirection();

        if (side == direction || side == direction.getOpposite()) {
            return new int[] {};
        }
        int[] slots = new int[inventory.size()];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
        return slots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
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
