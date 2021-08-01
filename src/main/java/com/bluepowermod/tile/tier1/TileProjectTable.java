/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BlockEntityType;
import net.minecraft.util.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements WorldlyContainer, MenuProvider {

    public final static int SLOTS = 28;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(18, ItemStack.EMPTY);
    protected NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);

    public TileProjectTable() {
        super(BPBlockEntityType.PROJECT_TABLE);
    }

    public TileProjectTable(BlockEntityType<?> type) {
        super(type);
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
    public CompoundTag save(CompoundTag tag) {

        super.save(tag);

        ListNBT tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte)currentIndex);
                inventory.get(currentIndex).save(tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("Items", tagList);

        tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < craftingGrid.size(); ++currentIndex) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) currentIndex);
                craftingGrid.get(currentIndex).save(tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("CraftingGrid", tagList);
        return tag;
    }

    @Override
    public void load(BlockState blockState, CompoundTag tag) {

        super.load(blockState, tag);

        ListNBT tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(19, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.of(tagCompound));
            }
        }

        tagList = tag.getList("CraftingGrid", 10);
        craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < craftingGrid.size()) {
                craftingGrid.set(slot, ItemStack.of(tagCompound));
            }
        }
    }

    @Override
    public int getContainerSize() {

        return inventory.size();
    }

    @Override
    public ItemStack getItem(int i) {
        return i < 18 ? inventory.get(i) : craftingGrid.get(i - 18);
    }

    public ItemStack getStackInCraftingSlot(int i) {
        return craftingGrid.get(i);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {

        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = getItem(i);
        if (!itemStack.isEmpty()) {
            setItem(i, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
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
    public int getMaxStackSize() {

        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.blockPosition().closerThan(worldPosition, 64.0D);
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {

    }

    @Override
    public boolean canPlaceItem(int p_94041_1_, ItemStack p_94041_2_) {

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
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return new StringTextComponent(Refs.PROJECTTABLE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, PlayerInventory inventory, Player player) {
        return new ContainerProjectTable(id, inventory, this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, getContainerSize() - 1).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < getContainerSize();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index < getContainerSize();
    }
}
