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
import com.bluepowermod.tile.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author MineMaarten
 */
public class TileManager extends TileMachineBase implements WorldlyContainer, IRejectAnimator, IFuzzyRetrieving, IGuiButtonSensitive {
    public static final int SLOTS = 25;
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public TubeColor filterColor = TubeColor.NONE;
    public int priority;
    public int mode;
    public int fuzzySetting;
    private int rejectTicker = -1;

    public TileManager(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.MANAGER, pos, state);
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
    public void onButtonPress(Player player, int messageId, int value) {

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

    public static void tickManager(Level level, BlockPos pos, BlockState state, TileManager blockEntity) {

        if (!level.isClientSide && blockEntity.getTicker() % BUFFER_EMPTY_INTERVAL == 0) {
            blockEntity.dumpUnwantedItems();
            blockEntity.retrieveItemsFromManagers();
            blockEntity.setOutputtingRedstone(blockEntity.mode == 0 && blockEntity.shouldEmitRedstone());
        }
        if (blockEntity.rejectTicker >= 0) {
            if (++blockEntity.rejectTicker > ANIMATION_TIME) {
                blockEntity.rejectTicker = -1;
                blockEntity.markForRenderUpdate();
            }
        }
        TileBase.tickTileBase(level, pos, state, blockEntity);
    }

    private boolean shouldEmitRedstone() {

        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && acceptedItems(stack) > 0)
                return false;
        }
        return true;
    }

    private void retrieveItemsFromManagers() {
        //Optional<IMultipartContainer> container = MultipartHelper.getContainer(world, worldPosition.relative(getOutputDirection()));
        //if (container.isPresent()) {
        //}
    }

    private void dumpUnwantedItems() {

        BlockEntity te = getTileCache(getFacingDirection());
        Container inv = IOHelper.getInventoryForTE(te);
        int[] slots = IOHelper.getAccessibleSlotsForInventory(inv, getFacingDirection().getOpposite());
        for (int slot : slots) {
            ItemStack stack = inv.getItem(slot);
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
    public void load(CompoundTag tCompound) {

        super.load(tCompound);

        for (int i = 0; i < 24; i++) {
            CompoundTag tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.of(tc));
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
    public CompoundTag save(CompoundTag tCompound) {

        super.save(tCompound);

        for (int i = 0; i < 24; i++) {
                CompoundTag tc = new CompoundTag();
                inventory.get(i).save(tc);
                tCompound.put("inventory" + i, tc);
        }

        tCompound.putByte("filterColor", (byte) filterColor.ordinal());
        tCompound.putByte("mode", (byte) mode);
        tCompound.putByte("priority", (byte) priority);
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);
        return tCompound;
    }

    @Override
    public void writeToPacketNBT(CompoundTag tag) {

        super.writeToPacketNBT(tag);
        tag.putByte("rejectAnimation", (byte) rejectTicker);
    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {

        super.readFromPacketNBT(tag);
        rejectTicker = tag.getByte("rejectAnimation");
    }

    @Override
    public int getContainerSize() {

        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
    }

    @Override
    public ItemStack getItem(int i) {

        return inventory.get(i);
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
        if (itemStack != ItemStack.EMPTY) {
            setItem(i, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

        inventory.set(i, itemStack);
    }

    @Override
    public int getMaxStackSize() {

        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return worldPosition.closerThan(new Vec3i(player.getX(), player.getY(), player.getZ()), 64.0D);
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
    public void clearContent() {

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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
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
