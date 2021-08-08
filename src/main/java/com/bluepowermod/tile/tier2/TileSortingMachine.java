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
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MineMaarten
 */

public class TileSortingMachine extends TileMachineBase implements WorldlyContainer, IGuiButtonSensitive, MenuProvider {

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

    public TileSortingMachine(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.SORTING_MACHINE, pos, state);
        for (int i = 0; i < colors.length; i++)
            colors[i] = TubeColor.NONE;
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent(Refs.SORTING_MACHINE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
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

    public static void tickSorting(Level level, BlockPos pos, BlockState state, TileSortingMachine tileSortingMachine) {

        tileSortingMachine.nonAcceptedStack = ItemStack.EMPTY;
        TileBase.tickTileBase(level, pos, state, tileSortingMachine);
        if (!tileSortingMachine.sweepTriggered && tileSortingMachine.savedPulses > 0) {
            tileSortingMachine.savedPulses--;
            tileSortingMachine.sweepTriggered = true;
        }
        if (!level.isClientSide && level.getGameTime() % TileMachineBase.BUFFER_EMPTY_INTERVAL == 0
                && (tileSortingMachine.pullMode == PullMode.SINGLE_SWEEP && tileSortingMachine.sweepTriggered || tileSortingMachine.pullMode == PullMode.AUTOMATIC)) {
            tileSortingMachine.triggerSorting();
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
            BlockEntity inputTE = getTileCache(dir);// might need opposite

            if (inputTE instanceof Container) {
                Container inputInv = (Container) inputTE;
                int[] accessibleSlots;
                if (inputInv instanceof WorldlyContainer) {
                    accessibleSlots = ((WorldlyContainer) inputInv).getSlotsForFace(dir.getOpposite());
                } else {
                    accessibleSlots = new int[inputInv.getContainerSize()];
                    for (int i = 0; i < accessibleSlots.length; i++)
                        accessibleSlots[i] = i;
                }
                for (int slot : accessibleSlots) {
                    ItemStack stack = inputInv.getItem(slot);
                    if (!stack.isEmpty() && IOHelper.canTakeItemThroughFaceFromInventory(inputInv, stack, slot, dir.getOpposite().ordinal())) {
                        if (tryProcessItem(stack, false)) {
                            if (stack.getCount() == 0)
                                inputInv.setItem(slot, ItemStack.EMPTY);
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
    public void onButtonPress(Player player, int messageId, int value) {

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

    private boolean matchAndProcessColumn(Container inputInventory, int[] accessibleSlots, int column) {

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
                ItemStack invStack = inputInventory.getItem(slot);
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
                        ItemStack invStack = inputInventory.getItem(slot);
                        if (invStack != ItemStack.EMPTY && ItemStackHelper.areStacksEqual(invStack, stack, fuzzySettings[column])) {
                            int substracted = Math.min(stack.getCount(), invStack.getCount());
                            stack.setCount(stack.getCount() - substracted);
                            invStack.setCount(invStack.getCount() - substracted);
                            if (invStack.getCount() <= 0) {
                                inputInventory.setItem(slot, ItemStack.EMPTY);
                            }
                            ItemStack bufferStack = invStack.copy();
                            bufferStack.setCount(substracted);
                            addItemToOutputBuffer(bufferStack, colors[column]);
                        }
                    }
                }
            }
            inputInventory.setChanged();
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
    public CompoundTag save(CompoundTag tag) {

        super.save(tag);

        tag.putByte("pullMode", (byte) pullMode.ordinal());
        tag.putByte("sortMode", (byte) sortMode.ordinal());
        tag.putInt("savedPulses", savedPulses);

        int[] colorArray = new int[colors.length];
        for (int i = 0; i < colorArray.length; i++) {
            colorArray[i] = colors[i].ordinal();
        }
        tag.putIntArray("colors", colorArray);

        tag.putIntArray("fuzzySettings", fuzzySettings);

        ListTag tagList = new ListTag();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
            if (!inventory.get(currentIndex).isEmpty()) {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) currentIndex);
                inventory.get(currentIndex).save(tagCompound);
                tagList.add(tagCompound);
            }
        }
        tag.put("Items", tagList);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);

        pullMode = PullMode.values()[tag.getByte("pullMode")];
        sortMode = SortMode.values()[tag.getByte("sortMode")];
        savedPulses = tag.getInt("savedPulses");

        int[] colorArray = tag.getIntArray("colors");
        for (int i = 0; i < colorArray.length; i++) {
            colors[i] = TubeColor.values()[colorArray[i]];
        }

        if (tag.contains("fuzzySettings"))
            fuzzySettings = tag.getIntArray("fuzzySettings");

        ListTag tagList = tag.getList("Items", 10);
        inventory = NonNullList.withSize(40, ItemStack.EMPTY);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag tagCompound = tagList.getCompound(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, ItemStack.of(tagCompound));
            }
        }
    }

    @Override
    public int getContainerSize() {

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
    public ItemStack getItem(int slot) {

        return slot < inventory.size() ? inventory.get(slot) : ItemStack.EMPTY;
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
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            setItem(slot, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {

        if (slot < inventory.size()) {
            inventory.set(slot, itemStack);
            if (!itemStack.isEmpty() && itemStack.getCount() > getMaxStackSize()) {
                itemStack.setCount(getMaxStackSize());
            }
        } else {
            if (!itemStack.isEmpty())
                tryProcessItem(itemStack, false);
        }
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
    public boolean canPlaceItem(int var1, ItemStack var2) {

        return var1 < inventory.size() ? true : !var2.isEmpty() && tryProcessItem(var2, true);
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { inventory.size() };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return getOutputDirection().getOpposite() == direction && canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }

}
