/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.container.ContainerRegulator;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
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
public class TileRegulator extends TileMachineBase implements WorldlyContainer, IGuiButtonSensitive, MenuProvider {

    public static final int SLOTS = 27;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public TubeColor color = TubeColor.NONE;
    public int mode;
    public int fuzzySetting;

    public TileRegulator(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.REGULATOR.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.REGULATOR_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerRegulator(id, inventory, this);
    }

    private enum EnumSection {
        INPUT_FILTER, BUFFER, OUTPUT_FILTER
    }

    public static void tickRegulator(Level level, BlockPos pos, BlockState state, TileRegulator tileRegulator) {
        TileBase.tickTileBase(level, pos, state, tileRegulator);
        if (!level.isClientSide) {
            boolean ratiosMatch = true;
            for (int i = 0; i < 9; i++) {
                if (!tileRegulator.inventory.get(i).isEmpty()) {
                    int inputFilterItems = tileRegulator.getItemsInSection(tileRegulator.inventory.get(i), EnumSection.INPUT_FILTER);
                    int bufferItems = tileRegulator.getItemsInSection(tileRegulator.inventory.get(i), EnumSection.BUFFER);
                    if (bufferItems < inputFilterItems) {
                        ratiosMatch = false;
                        break;
                    }
                }
            }
            if (ratiosMatch && !tileRegulator.isEjecting())
                tileRegulator.checkIndividualOutputFilterAndEject();

            if (tileRegulator.mode == 1 && !tileRegulator.isEjecting()) {// supply mode
                Container inv = IOHelper.getInventoryForTE(tileRegulator.getTileCache(tileRegulator.getOutputDirection()));
                if (inv != null) {
                    int[] accessibleSlots;
                    if (inv instanceof WorldlyContainer) {
                        accessibleSlots = ((WorldlyContainer) inv).getSlotsForFace(tileRegulator.getFacingDirection());
                    } else {
                        accessibleSlots = new int[inv.getContainerSize()];
                        for (int i = 0; i < accessibleSlots.length; i++)
                            accessibleSlots[i] = i;
                    }
                    for (int i = 18; i < 27; i++) {
                        if (!tileRegulator.inventory.get(i).isEmpty()) {
                            int outputFilterItems = tileRegulator.getItemsInSection(tileRegulator.inventory.get(i), EnumSection.OUTPUT_FILTER);
                            int supplyingInvCount = 0;
                            for (int slot : accessibleSlots) {
                                ItemStack stackInSlot = inv.getItem(slot);
                                if (!stackInSlot.isEmpty() && ItemStackHelper.areStacksEqual(stackInSlot, tileRegulator.inventory.get(i), tileRegulator.fuzzySetting)
                                        && IOHelper.canPlaceItemThroughFaceToInventory(inv, tileRegulator.inventory.get(i), slot, tileRegulator.getFacingDirection().ordinal())) {
                                    supplyingInvCount += stackInSlot.getCount();
                                }
                            }
                            if (supplyingInvCount < outputFilterItems) {
                                ItemStack requestedStack = tileRegulator.inventory.get(i).copy();
                                requestedStack.setCount(outputFilterItems - supplyingInvCount);
                                ItemStack bufferItems = IOHelper.extract(tileRegulator, null, requestedStack, true, false, tileRegulator.fuzzySetting);// try
                                                                                                                                                  // to
                                                                                                                                                  // extract
                                // the items
                                // needed to fully
                                // supply the
                                // inventory from
                                // the buffer.
                                if (!bufferItems.isEmpty()) {
                                    ItemStack remainder = IOHelper.insert(inv, bufferItems, tileRegulator.getFacingDirection().ordinal(), false);// insert into
                                                                                                                                   // supplying inv.
                                    if (!remainder.isEmpty()) {
                                        IOHelper.insert(tileRegulator, remainder, null, false);// when not every item can be supplied, return
                                                                                                        // those to the buffer.
                                    }
                                }
                            }
                        }
                    }
                }
            }
            boolean shouldEmitRedstone = tileRegulator.isSatisfied() || tileRegulator.animationTicker >= 0;
            if (tileRegulator.isEjecting() != shouldEmitRedstone) {
                tileRegulator.setOutputtingRedstone(shouldEmitRedstone);
                tileRegulator.sendUpdatePacket();
            }
        }
    }

    @Override
    public void onButtonPress(Player player, int messageId, int value) {

        if (messageId == 1) {
            mode = value;
        } else if (messageId == 0) {
            color = TubeColor.values()[value];
        } else if (messageId == 2) {
            fuzzySetting = value;
        }
    }

    @Override
    public boolean isEjecting() {

        return super.isEjecting() || getOutputtingRedstone() > 0;
    }

    /**
     * Returns true if the supplying inventory has the items stated in the output filter.
     * 
     * @return
     */
    private boolean isSatisfied() {

        Container inv = IOHelper.getInventoryForTE(getTileCache(getOutputDirection()));
        if (inv != null) {
            int[] accessibleSlots;
            if (inv instanceof WorldlyContainer) {
                accessibleSlots = ((WorldlyContainer) inv).getSlotsForFace(getFacingDirection());
            } else {
                accessibleSlots = new int[inv.getContainerSize()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            boolean everythingNull = true;
            for (int i = 18; i < 27; i++) {
                if (!inventory.get(i).isEmpty()) {
                    everythingNull = false;
                    int outputFilterItems = getItemsInSection(inventory.get(i), EnumSection.OUTPUT_FILTER);
                    int supplyingInvCount = 0;
                    for (int slot : accessibleSlots) {
                        ItemStack stackInSlot = inv.getItem(slot);
                        if (!stackInSlot.isEmpty() && ItemStackHelper.areStacksEqual(stackInSlot, inventory.get(i), fuzzySetting)
                                && IOHelper.canPlaceItemThroughFaceToInventory(inv, inventory.get(i), slot, getFacingDirection().ordinal())) {
                            supplyingInvCount += stackInSlot.getCount();
                        }
                    }
                    if (supplyingInvCount < outputFilterItems)
                        return false;
                }
            }
            return !everythingNull;
        }
        return false;
    }

    private void checkIndividualOutputFilterAndEject() {

        // Check in output filter for every slot and look if the items are present in the buffer.
        for (int i = 0; i < 9; i++) {
            if (!inventory.get(i).isEmpty()) {
                int inputFilterItems = getItemsInSection(inventory.get(i), EnumSection.INPUT_FILTER);
                int bufferItems = getItemsInSection(inventory.get(i), EnumSection.BUFFER);
                if (bufferItems >= inputFilterItems) {
                    ItemStack stackFromBuffer = IOHelper.extract(this, null, inventory.get(i), true, false, fuzzySetting);
                    this.addItemToOutputBuffer(stackFromBuffer, color);
                }
            }
        }
    }
    

    private int getItemsInSection(ItemStack type, EnumSection section) {

        int count = 0;
        for (int i = section.ordinal() * 9; i < section.ordinal() * 9 + 9; i++) {
            if (!inventory.get(i).isEmpty() && ItemStackHelper.areStacksEqual(type, inventory.get(i), fuzzySetting))
                count += inventory.get(i).getCount();
        }
        return count;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (int i = 9; i < 18; i++) {
            if (!inventory.get(i).isEmpty())
                drops.add(inventory.get(i));
        }
        return drops;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tag.putByte("filterColor", (byte) color.ordinal());
        tag.putByte("mode", (byte) mode);
        tag.putByte("fuzzySetting", (byte) fuzzySetting);

        ListTag tagList = new ListTag();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).save(provider, tagCompound);
                tagList.add(tagCompound);
        }
        tag.put("Items", tagList);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        color = TubeColor.values()[tag.getByte("filterColor")];
        mode = tag.getByte("mode");
        fuzzySetting = tag.getByte("fuzzySetting");

        ListTag tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(27, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.parseOptional(provider, tagCompound));
            }
        }
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
        return true;
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
    public int[] getSlotsForFace(Direction side) {
        if (side == getFacingDirection() || side == getOutputDirection())
            return new int[0];
        int[] slots = new int[9];
        for (int i = 9; i < 18; i++)
            slots[i - 9] = i;
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
        return inventory.size() == 0;
    }

    @Override
    public void clearContent() {

    }

}
