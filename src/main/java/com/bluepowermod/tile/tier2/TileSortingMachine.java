/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tile.TileMachineBase;

/**
 * 
 * @author MineMaarten
 */

public class TileSortingMachine extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive {

    private ItemStack[] inventory = new ItemStack[40];
    public int curColumn = 0;
    public PullMode pullMode = PullMode.SINGLE_STEP;
    public SortMode sortMode = SortMode.ANYSTACK_SEQUENTIAL;
    private boolean sweepTriggered;
    private int savedPulses;
    public final TubeColor[] colors = new TubeColor[9];
    public int[] fuzzySettings = new int[8];
    private ItemStack nonAcceptedStack;//will be set to the latest accepted stack via tubes.. It will reject any following items from that stack that tick.

    public TileSortingMachine() {

        for (int i = 0; i < colors.length; i++)
            colors[i] = TubeColor.NONE;
    }

    public enum PullMode {
        SINGLE_STEP("single_step"), AUTOMATIC("automatic"), SINGLE_SWEEP("single_sweep");

        private final String name;

        private PullMode(String name) {

            this.name = name;
        }

        @Override
        public String toString() {

            return "gui.pullMode." + name;
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

            return "gui.sortMode." + name;
        }
    }

    @Override
    public void updateEntity() {
        nonAcceptedStack = null;
        super.updateEntity();
        if (!sweepTriggered && savedPulses > 0) {
            savedPulses--;
            sweepTriggered = true;
        }
        if (!worldObj.isRemote && worldObj.getWorldTime() % TileMachineBase.BUFFER_EMPTY_INTERVAL == 0
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

        if (isBufferEmpty()) {
            ForgeDirection dir = getOutputDirection().getOpposite();
            TileEntity inputTE = getTileCache(dir);// might need opposite

            if (inputTE instanceof IInventory) {
                IInventory inputInv = (IInventory) inputTE;
                int[] accessibleSlots;
                if (inputInv instanceof ISidedInventory) {
                    accessibleSlots = ((ISidedInventory) inputInv).getAccessibleSlotsFromSide(dir.getOpposite().ordinal());
                } else {
                    accessibleSlots = new int[inputInv.getSizeInventory()];
                    for (int i = 0; i < accessibleSlots.length; i++)
                        accessibleSlots[i] = i;
                }
                for (int slot : accessibleSlots) {
                    ItemStack stack = inputInv.getStackInSlot(slot);
                    if (stack != null && IOHelper.canExtractItemFromInventory(inputInv, stack, slot, dir.getOpposite().ordinal())) {
                        if (tryProcessItem(stack, false)) {
                            if (stack.stackSize == 0)
                                inputInv.setInventorySlotContents(slot, null);
                            return;
                        }
                    }
                }
                if (sortMode == SortMode.ANYSTACK_SEQUENTIAL) {
                    for (int i = curColumn; i < inventory.length; i += 8) {
                        ItemStack filterStack = inventory[i];
                        if (filterStack != null) {
                            ItemStack extractedStack = IOHelper.extract(inputTE, dir.getOpposite(), filterStack, true, false);
                            if (extractedStack != null) {
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
    }

    private boolean matchAndProcessColumn(IInventory inputInventory, int[] accessibleSlots, int column) {

        List<ItemStack> requirements = new ArrayList<ItemStack>();
        for (int i = 0; i < 5; i++) {
            ItemStack filterStack = inventory[column + 8 * i];
            if (filterStack != null) {
                boolean duplicate = false;
                for (ItemStack requirement : requirements) {
                    if (ItemStackHelper.areStacksEqual(requirement, filterStack, fuzzySettings[column])) {
                        requirement.stackSize += filterStack.stackSize;
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
                if (invStack != null && ItemStackHelper.areStacksEqual(invStack, stack, fuzzySettings[column])) {
                    stack.stackSize -= invStack.stackSize;
                    if (stack.stackSize <= 0) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        if (requirements.isEmpty()) {
            for (ItemStack stack : copy) {
                for (int slot : accessibleSlots) {
                    if (stack.stackSize > 0) {
                        ItemStack invStack = inputInventory.getStackInSlot(slot);
                        if (invStack != null && ItemStackHelper.areStacksEqual(invStack, stack, fuzzySettings[column])) {
                            int substracted = Math.min(stack.stackSize, invStack.stackSize);
                            stack.stackSize -= substracted;
                            invStack.stackSize -= substracted;
                            if (invStack.stackSize <= 0) {
                                inputInventory.setInventorySlotContents(slot, null);
                            }
                            ItemStack bufferStack = invStack.copy();
                            bufferStack.stackSize = substracted;
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
            for (int i = 0; i < inventory.length; i++) {
                ItemStack filter = inventory[i];
                if (filter != null && ItemStackHelper.areStacksEqual(filter, stack, fuzzySettings[i % 8]) && stack.stackSize >= filter.stackSize) {
                    if (!simulate) {
                        addItemToOutputBuffer(filter.copy(), colors[i % 8]);
                    }
                    stack.stackSize -= filter.stackSize;
                    return true;
                }
            }
            if (sortMode == SortMode.ANY_ITEM_DEFAULT) {
                if (!simulate) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
                }
                stack.stackSize = 0;
                return true;
            }
            break;
        case ANY_STACK:
        case ANY_STACK_DEFAULT:
            for (int i = 0; i < inventory.length; i++) {
                ItemStack filter = inventory[i];
                if (filter != null && ItemStackHelper.areStacksEqual(filter, stack, fuzzySettings[i % 8])) {
                    if (!simulate) {
                        addItemToOutputBuffer(stack.copy(), colors[i % 8]);
                    }
                    stack.stackSize = 0;
                    return true;
                }
            }
            if (sortMode == SortMode.ANY_STACK_DEFAULT) {
                if (!simulate) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
                }
                stack.stackSize = 0;
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
            for (int i = curColumn; i < inventory.length; i += 8) {
                if (inventory[i] != null)
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
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

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

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        tag.setByte("pullMode", (byte) pullMode.ordinal());
        tag.setByte("sortMode", (byte) sortMode.ordinal());
        tag.setInteger("savedPulses", savedPulses);

        int[] colorArray = new int[colors.length];
        for (int i = 0; i < colorArray.length; i++) {
            colorArray[i] = colors[i].ordinal();
        }
        tag.setIntArray("colors", colorArray);

        tag.setIntArray("fuzzySettings", fuzzySettings);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        pullMode = PullMode.values()[tag.getByte("pullMode")];
        sortMode = SortMode.values()[tag.getByte("sortMode")];
        savedPulses = tag.getInteger("savedPulses");

        int[] colorArray = tag.getIntArray("colors");
        for (int i = 0; i < colorArray.length; i++) {
            colors[i] = TubeColor.values()[colorArray[i]];
        }

        if (tag.hasKey("fuzzySettings"))
            fuzzySettings = tag.getIntArray("fuzzySettings");

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[40];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.length + 1;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slot) {

        return slot < inventory.length ? inventory[slot] : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;

    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        if (slot < inventory.length) {
            inventory[slot] = itemStack;
            if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
                itemStack.stackSize = getInventoryStackLimit();
            }
        } else {
            if (itemStack != null)
                tryProcessItem(itemStack, false);
        }
    }

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {
        if (from == getOutputDirection()) {
            return super.acceptItemFromTube(stack, from, simulate);
        } else if (!isBufferEmpty() && !ejectionScheduled) {
            return stack;
        } else {
            boolean success = !ItemStack.areItemStacksEqual(stack.stack, nonAcceptedStack) && tryProcessItem(stack.stack, simulate);
            if (success) {
                nonAcceptedStack = stack.stack;
                if (stack.stack.stackSize <= 0) {
                    return null;
                } else {
                    return stack;
                }
            } else {
                return stack;
            }
        }

    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {

        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {

        return var1 < inventory.length ? true : isBufferEmpty() && var2 != null && tryProcessItem(var2, true);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {

        return new int[] { inventory.length };
    }

    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3) {

        return getOutputDirection().getOpposite().ordinal() == var3 && isItemValidForSlot(var1, var2);
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3) {

        return false;
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.sorting_machine.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }

}
