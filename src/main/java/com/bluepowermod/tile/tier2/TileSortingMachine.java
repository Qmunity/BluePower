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
import com.bluepowermod.container.ContainerSortingMachine;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MineMaarten
 */

public class TileSortingMachine extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive, INamedContainerProvider {

    public int curColumn = 0;
    public PullMode pullMode = PullMode.SINGLE_STEP;
    public SortMode sortMode = SortMode.ANYSTACK_SEQUENTIAL;
    private boolean sweepTriggered;
    private int savedPulses;
    public static final int SLOTS = 40;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    public final TubeColor[] colors = new TubeColor[9];
    public int[] fuzzySettings = new int[8];
    private ItemStack nonAcceptedStack;// will be set to the latest accepted stack via tubes.. It will reject any following items from that stack that

    // tick.

    public TileSortingMachine() {
        super(BPTileEntityType.SORTING_MACHINE);
        for (int i = 0; i < colors.length; i++)
            colors[i] = TubeColor.NONE;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.SORTING_MACHINE_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ContainerSortingMachine(id, inventory, this);
    }

    public enum PullMode {
        SINGLE_STEP("single_step"), AUTOMATIC("automatic"), SINGLE_SWEEP("single_sweep");

        private final String name;

        private PullMode(String name) {

            this.name = name;
        }

        @Override
        public String toString() {

            return "gui.bluepower:sortingMachine.pullMode." + name;
        }
    }

    public enum SortMode {
        ANYSTACK_SEQUENTIAL("any_stack_sequential"), ALLSTACK_SEQUENTIAL("all_stacks_sequential"), RANDOM_ALLSTACKS("all_stacks_random"), ANY_ITEM(
                "any_item"), ANY_ITEM_DEFAULT("any_item_default"), ANY_STACK("any_stack"), ANY_STACK_DEFAULT("any_stack_default");

        private final String name;

        private SortMode(String name) {

            this.name = name;
        }

        @Override
        public String toString() {

            return "gui.bluepower:sortingMachine.sortMode." + name;
        }
    }

    @Override
    public void tick() {

        nonAcceptedStack = ItemStack.EMPTY;
        super.tick();
        if (!sweepTriggered && savedPulses > 0) {
            savedPulses--;
            sweepTriggered = true;
        }
        if (!world.isRemote && world.getGameTime() % TileMachineBase.BUFFER_EMPTY_INTERVAL == 0
                && (pullMode == PullMode.SINGLE_SWEEP && sweepTriggered || pullMode == PullMode.AUTOMATIC)) {
            triggerSorting();
        }

    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);
        if (newValue) {
            if (pullMode == PullMode.SINGLE_STEP)
                triggerSorting();
            if (pullMode == PullMode.SINGLE_SWEEP)
                savedPulses++;
        }

    }

    private void triggerSorting() {

            Direction dir = getOutputDirection().getOpposite();
            TileEntity inputTE = getTileCache(dir);// might need opposite

            if (inputTE instanceof IInventory) {
                IInventory inputInv = (IInventory) inputTE;
                int[] accessibleSlots;
                if (inputInv instanceof ISidedInventory) {
                    accessibleSlots = ((ISidedInventory) inputInv).getSlotsForFace(dir.getOpposite());
                } else {
                    accessibleSlots = new int[inputInv.getSizeInventory()];
                    for (int i = 0; i < accessibleSlots.length; i++)
                        accessibleSlots[i] = i;
                }
                for (int slot : accessibleSlots) {
                    ItemStack stack = inputInv.getStackInSlot(slot);
                    if (!stack.isEmpty() && IOHelper.canExtractItemFromInventory(inputInv, stack, slot, dir.getOpposite().ordinal())) {
                        if (tryProcessItem(stack, false)) {
                            if (stack.getCount() == 0)
                                inputInv.setInventorySlotContents(slot, ItemStack.EMPTY);
                            return;
                        }
                    }
                }
                if (sortMode == SortMode.ANYSTACK_SEQUENTIAL) {
                    for (int i = curColumn; i < inventory.size(); i += 8) {
                        ItemStack filterStack = inventory.get(i);
                        if (!filterStack.isEmpty()) {
                            ItemStack extractedStack = IOHelper.extract(inputTE, dir.getOpposite(), filterStack, true, false);
                            if (!extractedStack.isEmpty()) {
                                addItemToOutputBuffer(extractedStack.copy(), colors[curColumn]);
                                gotoNextNonEmptyColumn();
                                break;
                            }
                        }
                    }
                } else if (sortMode == SortMode.ALLSTACK_SEQUENTIAL) {
                    if (matchAndProcessColumn(inputInv, accessibleSlots, curColumn)) {
                        gotoNextNonEmptyColumn();
                    }
                } else if (sortMode == SortMode.RANDOM_ALLSTACKS) {
                    for (int i = 0; i < 8; i++) {
                        if (matchAndProcessColumn(inputInv, accessibleSlots, i)) {
                            break;
                        }
                    }
                }
            }
    }

    @Override
    public void onButtonPress(PlayerEntity player, int messageId, int value) {

        if (messageId < 0)
            return;

        if (messageId < 9) {
            colors[messageId] = TubeColor.values()[value];
        } else if (messageId == 9) {
            pullMode = PullMode.values()[value];
        } else if (messageId == 10) {
            sortMode = SortMode.values()[value];
        } else {
            fuzzySettings[messageId - 11] = value;
        }
    }

    private boolean matchAndProcessColumn(IInventory inputInventory, int[] accessibleSlots, int column) {

        List<ItemStack> requirements = new ArrayList<ItemStack>();
        for (int i = 0; i < 5; i++) {
            ItemStack filterStack = inventory.get(column + 8 * i);
            if (!filterStack.isEmpty()) {
                boolean duplicate = false;
                for (ItemStack requirement : requirements) {
                    if (ItemStackHelper.areStacksEqual(requirement, filterStack, fuzzySettings[column])) {
                        requirement.setCount(requirement.getCount() + filterStack.getCount());
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate)
                    requirements.add(filterStack.copy());
            }
        }
        if (requirements.size() == 0)
            return false;
        ItemStack[] copy = new ItemStack[requirements.size()];
        for (int i = 0; i < copy.length; i++)
            copy[i] = requirements.get(i).copy();

        Iterator<ItemStack> iterator = requirements.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            for (int slot : accessibleSlots) {
                ItemStack invStack = inputInventory.getStackInSlot(slot);
                if (!invStack.isEmpty() && ItemStackHelper.areStacksEqual(invStack, stack, fuzzySettings[column])) {
                    stack.setCount(stack.getCount() - invStack.getCount());
                    if (stack.getCount() <= 0) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        if (requirements.isEmpty()) {
            for (ItemStack stack : copy) {
                for (int slot : accessibleSlots) {
                    if (stack.getCount() > 0) {
                        ItemStack invStack = inputInventory.getStackInSlot(slot);
                        if (invStack != ItemStack.EMPTY && ItemStackHelper.areStacksEqual(invStack, stack, fuzzySettings[column])) {
                            int substracted = Math.min(stack.getCount(), invStack.getCount());
                            stack.setCount(stack.getCount() - substracted);
                            invStack.setCount(invStack.getCount() - substracted);
                            if (invStack.getCount() <= 0) {
                                inputInventory.setInventorySlotContents(slot, ItemStack.EMPTY);
                            }
                            ItemStack bufferStack = invStack.copy();
                            bufferStack.setCount(substracted);
                            addItemToOutputBuffer(bufferStack, colors[column]);
                        }
                    }
                }
            }
            inputInventory.markDirty();
            return true;
        } else {
            return false;
        }

    }

    private boolean tryProcessItem(ItemStack stack, boolean simulate) {

        switch (sortMode) {
        case ANYSTACK_SEQUENTIAL:

            break;
        case ALLSTACK_SEQUENTIAL:
            break;
        case RANDOM_ALLSTACKS:
            break;
        case ANY_ITEM:
        case ANY_ITEM_DEFAULT:
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack filter = inventory.get(i);
                if (!filter.isEmpty() && ItemStackHelper.areStacksEqual(filter, stack, fuzzySettings[i % 8])
                        && stack.getCount() >= filter.getCount()) {
                    if (!simulate) {
                        ItemStack itemStack = stack.copy();
                        itemStack.setCount(filter.getCount());
                        addItemToOutputBuffer(itemStack, colors[i % 8]);
                    }
                    stack.setCount(stack.getCount() - filter.getCount());
                    return true;
                }
            }
            if (sortMode == SortMode.ANY_ITEM_DEFAULT) {
                if (!simulate) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
                }
                stack.setCount(0);
                return true;
            }
            break;
        case ANY_STACK:
        case ANY_STACK_DEFAULT:
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack filter = inventory.get(i);
                if (!filter.isEmpty() && ItemStackHelper.areStacksEqual(filter, stack, fuzzySettings[i % 8])) {
                    if (!simulate) {
                        addItemToOutputBuffer(stack.copy(), colors[i % 8]);
                    }
                    stack.setCount(0);
                    return true;
                }
            }
            if (sortMode == SortMode.ANY_STACK_DEFAULT) {
                if (!simulate) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
                }
                stack.setCount(0);
                return true;
            }
            break;

        }
        return false;
    }

    private void gotoNextNonEmptyColumn() {

        int oldColumn = curColumn++;
        if (curColumn > 7) {
            curColumn = 0;
            sweepTriggered = false;
        }
        while (oldColumn != curColumn) {
            for (int i = curColumn; i < inventory.size(); i += 8) {
                if (!inventory.get(i).isEmpty())
                    return;
            }
            if (++curColumn > 7) {
                curColumn = 0;
                sweepTriggered = false;
            }
        }
        curColumn = 0;
    }


    @Override
    public CompoundNBT write(CompoundNBT tag) {

        super.write(tag);

        tag.putByte("pullMode", (byte) pullMode.ordinal());
        tag.putByte("sortMode", (byte) sortMode.ordinal());
        tag.putInt("savedPulses", savedPulses);

        int[] colorArray = new int[colors.length];
        for (int i = 0; i < colorArray.length; i++) {
            colorArray[i] = colors[i].ordinal();
        }
        tag.putIntArray("colors", colorArray);

        tag.putIntArray("fuzzySettings", fuzzySettings);

        ListNBT tagList = new ListNBT();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
            if (!inventory.get(currentIndex).isEmpty()) {
                CompoundNBT tagCompound = new CompoundNBT();
                tagCompound.putByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).write(tagCompound);
                tagList.add(tagCompound);
            }
        }
        tag.put("Items", tagList);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {

        super.read(state, tag);

        pullMode = PullMode.values()[tag.getByte("pullMode")];
        sortMode = SortMode.values()[tag.getByte("sortMode")];
        savedPulses = tag.getInt("savedPulses");

        int[] colorArray = tag.getIntArray("colors");
        for (int i = 0; i < colorArray.length; i++) {
            colors[i] = TubeColor.values()[colorArray[i]];
        }

        if (tag.contains("fuzzySettings"))
            fuzzySettings = tag.getIntArray("fuzzySettings");

        ListNBT tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(40, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundNBT tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.read(tagCompound));
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slot) {

        return slot < inventory.size() ? inventory.get(slot) : ItemStack.EMPTY;
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
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            setInventorySlotContents(slot, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        if (slot < inventory.size()) {
            inventory.set(slot, itemStack);
            if (!itemStack.isEmpty() && itemStack.getCount() > getInventoryStackLimit()) {
                itemStack.setCount(getInventoryStackLimit());
            }
        } else {
            if (!itemStack.isEmpty())
                tryProcessItem(itemStack, false);
        }
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
    public boolean isItemValidForSlot(int var1, ItemStack var2) {

        return var1 < inventory.size() ? true : !var2.isEmpty() && tryProcessItem(var2, true);
    }

    @Override
    public void clear() {

    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { inventory.size() };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return getOutputDirection().getOpposite() == direction && isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }

}
