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

package com.bluepowermod.tile.tier1;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TileBuffer extends TileBase implements ISidedInventory {
    
    private final NonNullList<ItemStack> allInventories = NonNullList.withSize(21, ItemStack.EMPTY);
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(CompoundNBT tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 20; i++) {
            CompoundNBT tc = tCompound.getCompoundTag("inventory" + i);
            allInventories.set(i, new ItemStack(tc));
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT writeToNBT(CompoundNBT tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 20; i++) {
                CompoundNBT tc = new CompoundNBT();
                allInventories.get(i).writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
        }
        return  tCompound;
    }
    
    @Override
    public int getSizeInventory() {
    
        return allInventories.size();
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return allInventories.get(i);
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
        return getStackInSlot(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        allInventories.set(i, itemStack);
    }
    
    @Override
    public String getName() {
    
        return BPBlocks.buffer.getTranslationKey();
    }
    
    @Override
    public boolean hasCustomName() {
    
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getDistanceSqToCenter(pos) <= 64.0D;
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
        for (ItemStack stack : allInventories)
            if (!stack.isEmpty()) drops.add(stack);
        return drops;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int var1 = side.ordinal();
        Direction dir = getFacingDirection();
        if (side == dir) {
            int[] allSlots = new int[allInventories.size()];
            for (int i = 0; i < allSlots.length; i++)
                allSlots[i] = i;
            return allSlots;
        }
        if (var1 > dir.getOpposite().ordinal()) var1--;
        int[] slots = new int[4];
        for (int i = 0; i < 4; i++) {
            slots[i] = var1 + i * 5;
        }
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

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return allInventories.size() == 0;
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
}
