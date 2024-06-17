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
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TileBuffer extends TileBase implements WorldlyContainer, MenuProvider {

    public static final int SLOTS = 21;
    private final NonNullList<ItemStack> allInventories = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public TileBuffer(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BUFFER.get(), pos, state);
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void loadAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.loadAdditional(tCompound, provider);
        ContainerHelper.loadAllItems(tCompound.getCompound("inventory"), allInventories, provider);
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.saveAdditional(tCompound, provider);
        CompoundTag tc = new CompoundTag();
        ContainerHelper.saveAllItems(tc, allInventories, provider);
        tCompound.put("inventory", tc);
    }

    @Override
    public int getContainerSize() {
    
        return allInventories.size();
    }
    
    @Override
    public ItemStack getItem(int i) {
    
        return allInventories.get(i);
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
        return getItem(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
    
        allInventories.set(i, itemStack);
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
    public boolean canPlaceItem(int i, ItemStack itemStack) {
    
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return allInventories.size() == 0;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.BLOCKBUFFER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerBuffer(id, inventory, this);
    }
}
