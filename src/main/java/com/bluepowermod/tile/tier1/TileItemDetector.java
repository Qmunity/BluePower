/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.List;

;

/**
 * @author MineMaarten
 */
public class TileItemDetector extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive {

    public int mode;
    public final static int SLOTS = 10;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private int savedPulses = 0;
    public int fuzzySetting;

    public TileItemDetector() {
        super(BPTileEntityType.ITEM_DETECTOR);
    }

    @Override
    public void tick() {

        super.tick();
        if (!world.isRemote) {
            if (mode == 0 || mode == 1) {
                if (world.getGameTime() % 2 == 0) {
                    if (getOutputtingRedstone() > 0) {
                        this.setOutputtingRedstone(false);
                        sendUpdatePacket();
                    } else if (savedPulses > 0) {
                        savedPulses--;
                        setOutputtingRedstone(true);
                        sendUpdatePacket();
                    }
                }
            } else {
            }
        }
    }

    @Override
    public boolean isEjecting() {

        return getOutputtingRedstone() > 0;
    }

/*    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, EnumFacing from, boolean simulate) {

        if (from == getFacingDirection() && !isItemAccepted(stack.stack))
            return stack;
        TubeStack remainder = super.acceptItemFromTube(stack, from, simulate);
        if (remainder == null && !simulate && mode < 2) {
            savedPulses += mode == 0 ? stack.stack.getCount() : 1;
        }
        return remainder;
    }*/

    private boolean isItemAccepted(ItemStack item) {

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

        mode = tCompound.getByte("mode");
        fuzzySetting = tCompound.getByte("fuzzySetting");
        savedPulses = tCompound.getInt("savedPulses");
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

        tCompound.putByte("mode", (byte) mode);
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);
        tCompound.putInt("savedPulses", savedPulses);

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


    @Override
    public void onButtonPress(PlayerEntity player, int messageId, int value) {

        if (messageId == 0)
            mode = value;
        if (messageId == 1) {
            fuzzySetting = value;
        }
    }

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

}
