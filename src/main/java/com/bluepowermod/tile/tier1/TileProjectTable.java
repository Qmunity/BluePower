/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements IInventory, INamedContainerProvider {

    public final static int SLOTS = 28;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(18, ItemStack.EMPTY);
    protected NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);

    public TileProjectTable() {
        super(BPTileEntityType.PROJECT_TABLE);
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        for (ItemStack stack : craftingGrid)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        super.write(tag);

        ListNBT tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte)currentIndex);
                inventory.get(currentIndex).write(tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("Items", tagList);

        tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < craftingGrid.size(); ++currentIndex) {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte) currentIndex);
                craftingGrid.get(currentIndex).write(tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("CraftingGrid", tagList);
        return tag;
    }

    @Override
    public void read(CompoundNBT tag) {

        super.read(tag);

        ListNBT tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(19, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.read(tagCompound));
            }
        }

        tagList = tag.getList("CraftingGrid", 10);
        craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < craftingGrid.size()) {
                craftingGrid.set(slot, ItemStack.read(tagCompound));
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return i < 18 ? inventory.get(i) : craftingGrid.get(i - 18);
    }
    public ItemStack getStackInCraftingSlot(int i) {
        return craftingGrid.get(i);
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
        if(i < 18){
            inventory.set(i, itemStack);
        }else{
            craftingGrid.set(i - 18, itemStack);
        }
    }

    public void setCraftingSlotContents(int i, ItemStack itemStack) {
        craftingGrid.set(i, itemStack);
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : inventory ){
            if (!itemstack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.PROJECTTABLE_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerProjectTable(id, inventory, this);
    }
}
