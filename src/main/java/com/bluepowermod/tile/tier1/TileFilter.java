/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.container.ContainerFilter;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;
/**
 * @author MineMaarten
 */
public class TileFilter extends TileTransposer implements WorldlyContainer, IGuiButtonSensitive, MenuProvider {

    public static final int SLOTS = 9;
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public TubeColor filterColor = TubeColor.NONE;
    public int fuzzySetting;

/*    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, EnumFacing from, boolean simulate) {

        if (from == getFacingDirection() && (!isItemAccepted(stack.stack) || !isBufferEmpty()))
            return stack;
        if (!simulate)
            this.addItemToOutputBuffer(stack.stack, filterColor);
        return null;
    }*/

    @Override
    protected boolean isItemAccepted(ItemStack item) {

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

    @Override
    protected TubeColor getAcceptedItemColor(ItemStack item) {

        return filterColor;
    }

    @Override
    protected void pullItem() {
            Direction dir = getOutputDirection().getOpposite();
            BlockEntity tile = getTileCache(dir);
            Direction direction = dir.getOpposite();
            boolean everythingNull = true;
            for (ItemStack filterStack : inventory) {
                if (!filterStack.isEmpty()) {
                    everythingNull = false;
                    ItemStack extractedStack = IOHelper.extract(tile, direction, filterStack, true, false, fuzzySetting);
                    if (!extractedStack.isEmpty()) {
                        this.addItemToOutputBuffer(extractedStack, filterColor);
                        break;
                    }
                }
            }
            if (everythingNull) {
                ItemStack extractedStack = IOHelper.extract(tile, direction, false);
                if (!extractedStack.isEmpty()) {
                    this.addItemToOutputBuffer(extractedStack, filterColor);
                }
            }
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void load(BlockState blockState, CompoundTag tCompound) {
        super.load(blockState, tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundTag tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, new ItemStack((IItemProvider) tc));
        }
        filterColor = TubeColor.values()[tCompound.getByte("filterColor")];
        fuzzySetting = tCompound.getByte("fuzzySetting");
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundTag save(CompoundTag tCompound) {

        super.save(tCompound);

        for (int i = 0; i < 9; i++) {
                CompoundTag tc = new CompoundTag();
                inventory.get(i).save(tc);
                tCompound.put("inventory" + i, tc);
        }

        tCompound.putByte("filterColor", (byte) filterColor.ordinal());
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);

        return tCompound;
    }

    @Override
    public int getContainerSize() {

        return inventory.size();
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
        if (!itemStack.isEmpty()) {
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }


   /* @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            filterColor = TubeColor.values()[value];
        if (messageId == 1)
            fuzzySetting = value;
    }
*/
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
    public void clearContent() {

    }

    @Override
    public void onButtonPress(Player player, int messageId, int value) {
        if (messageId == 0)
            filterColor = TubeColor.values()[value];
        if (messageId == 1)
            fuzzySetting = value;
    }

    @Override
    public Component getDisplayName() {
        return new StringTextComponent(Refs.FILTER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, PlayerInventory inventory, Player playerEntity) {
        return new ContainerFilter(id, inventory, this);
    }
}
