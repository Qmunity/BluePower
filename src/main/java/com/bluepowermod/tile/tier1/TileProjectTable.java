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
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements WorldlyContainer, MenuProvider {

    public final static int SLOTS = 28;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(18, ItemStack.EMPTY);
    protected NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);

    public TileProjectTable(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.PROJECT_TABLE.get(), pos, state);
    }

    public TileProjectTable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag tItems = new CompoundTag();
        ContainerHelper.saveAllItems(tItems, inventory, provider);
        tag.put("Items", tItems);

        CompoundTag tCraftingGrid = new CompoundTag();
        ContainerHelper.saveAllItems(tCraftingGrid, craftingGrid, provider);
        tag.put("CraftingGrid", tCraftingGrid);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), inventory, provider);
        ContainerHelper.loadAllItems(tag.getCompound("CraftingGrid"), craftingGrid, provider);
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
        return Component.literal(Refs.PROJECTTABLE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
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
