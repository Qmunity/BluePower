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

import com.bluepowermod.container.ContainerBuffer;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class TileBuffer extends TileBase implements ISidedInventory, INamedContainerProvider {

    public static final int SLOTS = 21;
    private final NonNullList<ItemStack> allInventories = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public TileBuffer() {
        super(BPTileEntityType.BUFFER);
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void read(CompoundNBT tCompound) {
    
        super.read(tCompound);
        
        for (int i = 0; i < 20; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            allInventories.set(i, new ItemStack((IItemProvider) tc));
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT write(CompoundNBT tCompound) {
    
        super.write(tCompound);
        
        for (int i = 0; i < 20; i++) {
                CompoundNBT tc = new CompoundNBT();
                allInventories.get(i).write(tc);
                tCompound.put("inventory" + i, tc);
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
        return getStackInSlot(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        allInventories.set(i, itemStack);
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
    public NonNullList<ItemStack> getDrops() {
    
        NonNullList<ItemStack> drops = super.getDrops();
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
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.BLOCKBUFFER_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ContainerBuffer(id, inventory, this);
    }
}
