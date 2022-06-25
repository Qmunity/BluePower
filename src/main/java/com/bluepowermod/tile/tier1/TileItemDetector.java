/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.container.ContainerItemDetector;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */
public class TileItemDetector extends TileMachineBase implements WorldlyContainer, IGuiButtonSensitive, MenuProvider {

    public int mode;
    public final static int SLOTS = 10;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private int savedPulses = 0;
    public int fuzzySetting;

    public TileItemDetector(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.ITEM_DETECTOR, pos, state);
    }


    public static void tickItemDetector(Level level, BlockPos pos, BlockState state, TileItemDetector tileItemDetector) {

        TileBase.tickTileBase(level, pos, state, tileItemDetector);
        if (!level.isClientSide) {
            if (tileItemDetector.mode == 0 || tileItemDetector.mode == 1) {
                if (level.getGameTime() % 2 == 0) {
                    if (tileItemDetector.getOutputtingRedstone() > 0) {
                        tileItemDetector.setOutputtingRedstone(false);
                        tileItemDetector.sendUpdatePacket();
                    } else if (tileItemDetector.savedPulses > 0) {
                        tileItemDetector.savedPulses--;
                        tileItemDetector.setOutputtingRedstone(true);
                        tileItemDetector.sendUpdatePacket();
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
    public void load(CompoundTag tCompound) {
        super.load(tCompound);
        for (int i = 0; i < 9; i++) {
            CompoundTag tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.of(tc));
        }

        mode = tCompound.getByte("mode");
        fuzzySetting = tCompound.getByte("fuzzySetting");
        savedPulses = tCompound.getInt("savedPulses");
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound) {

        super.saveAdditional(tCompound);

        for (int i = 0; i < 9; i++) {
                CompoundTag tc = new CompoundTag();
                inventory.get(i).save(tc);
                tCompound.put("inventory" + i, tc);
        }

        tCompound.putByte("mode", (byte) mode);
        tCompound.putByte("fuzzySetting", (byte) fuzzySetting);
        tCompound.putInt("savedPulses", savedPulses);
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


    @Override
    public void onButtonPress(Player player, int messageId, int value) {

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

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent(Refs.ITEMDETECTOR_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerItemDetector(id, inventory, this);
    }
}
