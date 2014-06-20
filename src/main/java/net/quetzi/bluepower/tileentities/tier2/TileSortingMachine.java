package net.quetzi.bluepower.tileentities.tier2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.tube.IPneumaticTube.TubeColor;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.part.IGuiButtonSensitive;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.TileMachineBase;

/**
 * 
 * @author MineMaarten
 */

public class TileSortingMachine extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive {
    
    private ItemStack[]      inventory = new ItemStack[40];
    public int               curColumn = 0;
    public PullMode          pullMode  = PullMode.SINGLE_STEP;
    public SortMode          sortMode  = SortMode.ANYSTACK_SEQUENTIAL;
    private boolean          sweepTriggered;
    public final TubeColor[] colors    = new TubeColor[8];
    
    public TileSortingMachine() {
    
        for (int i = 0; i < 8; i++)
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
        ANYSTACK_SEQUENTIAL("any_stack_sequential"), ALLSTACK_SEQUENTIAL("all_stack_sequential"), RANDOM_ALLSTACKS("all_stacks_random"), ANY_ITEM("any_item"), ANY_ITEM_DEFAULT("any_item_default"), ANY_STACK("any_stack"), ANY_STACK_DEFAULT("any_stack_default");
        
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
    
        super.updateEntity();
        if (!worldObj.isRemote && worldObj.getWorldTime() % TileMachineBase.BUFFER_EMPTY_INTERVAL == 0 && (pullMode == PullMode.SINGLE_SWEEP && sweepTriggered || pullMode == PullMode.AUTOMATIC)) {
            triggerSorting();
        }
        
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        if (newValue) {
            if (pullMode == PullMode.SINGLE_STEP) triggerSorting();
            if (pullMode == PullMode.SINGLE_SWEEP) sweepTriggered = true;
        }
        
    }
    
    private void triggerSorting() {
    
        if (isBufferEmpty()) {
            ForgeDirection dir = getOutputDirection().getOpposite();
            TileEntity inputTE = getTileCache()[dir.ordinal()].getTileEntity();//might need opposite
            
            if (inputTE instanceof IInventory) {
                IInventory inputInv = (IInventory) inputTE;
                int[] accessibleSlots;
                if (inputInv instanceof ISidedInventory) {
                    accessibleSlots = ((ISidedInventory) inputInv).getAccessibleSlotsFromSide(dir.ordinal());
                } else {
                    accessibleSlots = new int[inputInv.getSizeInventory()];
                    for (int i = 0; i < accessibleSlots.length; i++)
                        accessibleSlots[i] = i;
                }
                boolean[] satisfiedFilters = new boolean[5];
                for (int slot : accessibleSlots) {
                    ItemStack stack = inputInv.getStackInSlot(slot);
                    if (stack != null && IOHelper.canExtractItemFromInventory(inputInv, stack, slot, dir.ordinal())) {
                        if (tryProcessItem(stack)) {
                            if (stack.stackSize == 0) inputInv.setInventorySlotContents(slot, null);
                            return;
                        } else {
                            for (int i = 0; i < 5; i++) {
                                if (!satisfiedFilters[i]) {
                                    ItemStack filterStack = inventory[curColumn + 8 * i];
                                    if (filterStack != null && filterStack.isItemEqual(stack) && stack.stackSize >= filterStack.stackSize) {
                                        satisfiedFilters[i] = true;
                                    }
                                }
                            }
                        }
                    }
                }
                
                for (int i = 0; i < satisfiedFilters.length; i++) {
                    if (sortMode == SortMode.ALLSTACK_SEQUENTIAL && !satisfiedFilters[i] && inventory[curColumn + 8 * i] != null) return;
                }
                for (int i = 0; i < satisfiedFilters.length; i++) {
                    if (satisfiedFilters[i]) {
                        ItemStack filter = inventory[curColumn + 8 * i];
                        addItemToOutputBuffer(filter.copy(), TubeColor.values()[curColumn]);
                        IOHelper.extract(inputTE, dir, filter, false);
                    }
                }
                gotoNextNonEmptyColumn();
            }
        }
        
    }
    
    private boolean tryProcessItem(ItemStack stack) {
    
        switch (sortMode) {
            case ANYSTACK_SEQUENTIAL:
                for (int i = curColumn; i < inventory.length; i += 8) {
                    if (inventory[i] != null && stack.isItemEqual(inventory[i])) {
                        addItemToOutputBuffer(stack.copy(), colors[i % 8]);
                        stack.stackSize = 0;
                        gotoNextNonEmptyColumn();
                        return true;
                    }
                }
                break;
            case ALLSTACK_SEQUENTIAL:
                break;
            case RANDOM_ALLSTACKS:
                break;
            case ANY_ITEM:
            case ANY_ITEM_DEFAULT:
                for (int i = 0; i < inventory.length; i++) {
                    ItemStack filter = inventory[i];
                    if (filter != null && stack.isItemEqual(filter) && stack.stackSize >= filter.stackSize) {
                        addItemToOutputBuffer(filter.copy(), colors[i % 8]);
                        stack.stackSize -= filter.stackSize;
                        return true;
                    }
                }
                if (sortMode == SortMode.ANY_ITEM_DEFAULT) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
                    stack.stackSize = 0;
                    return true;
                }
                break;
            case ANY_STACK:
            case ANY_STACK_DEFAULT:
                for (int i = 0; i < inventory.length; i++) {
                    ItemStack filter = inventory[i];
                    if (filter != null && stack.isItemEqual(filter)) {
                        addItemToOutputBuffer(stack.copy(), colors[i % 8]);
                        stack.stackSize = 0;
                        return true;
                    }
                }
                if (sortMode == SortMode.ANY_STACK_DEFAULT) {
                    addItemToOutputBuffer(stack.copy(), colors[8]);
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
                if (inventory[i] != null) return;
            }
            if (++curColumn > 7) {
                curColumn = 0;
                sweepTriggered = false;
            }
        }
        curColumn = 0;
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        if (messageId < 8) {
            colors[messageId] = TubeColor.values()[value];
        } else if (messageId == 9) {
            pullMode = PullMode.values()[value];
        } else {
            sortMode = SortMode.values()[value];
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        
        tag.setByte("pullMode", (byte) pullMode.ordinal());
        tag.setByte("sortMode", (byte) sortMode.ordinal());
        
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
    
        return inventory.length;
    }
    
    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int slot) {
    
        return inventory[slot];
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
    
        inventory[slot] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
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
    
        return false;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
    
        return new int[0];
    }
    
    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3) {
    
        return false;
    }
    
    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3) {
    
        return false;
    }
    
    @Override
    public String getInventoryName() {
    
        return Refs.SORTING_MACHINE_NAME;
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return false;
    }
    
}
