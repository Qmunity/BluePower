/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import java.util.List;

public class TileKinectGenerator extends TileBase implements ISidedInventory{

	public int windspeed = 10;
	public int windtick = 0;
	public static final int SLOTS = 1;
	public TileKinectGenerator(){
	    super(BPTileEntityType.KINETIC_GENERATOR);
	}

	@Override
	public void tick() {
		
        if (windspeed < 0){
			windtick +=windspeed;
		}
	}
    private final NonNullList<ItemStack> allInventories = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void read(CompoundNBT tCompound) {

        super.read(tCompound);

        for (int i = 0; i < 1; i++) {
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

        for (int i = 0; i < 1; i++) {
            if (!allInventories.get(i).isEmpty()) {
                CompoundNBT tc = new CompoundNBT();
                allInventories.get(i).write(tc);
                tCompound.put("inventory" + i, tc);
            }
        }
        return tCompound;
    }

    @Override
    public int getSizeInventory() {

        return allInventories.size();
    }


    @Override
    public ItemStack getStackInSlot(int i) {

        return this.allInventories.get(i);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        // this needs to be side aware as well
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

        this.allInventories.set(i, itemStack);
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getPosition().withinDistance(pos,64.0D);
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
    public boolean isEmpty() {
        return allInventories.isEmpty();
    }


    @Override
    public void clear() {

    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (!stack.isEmpty()) drops.add(stack);
        return drops;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }
}
