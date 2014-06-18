package net.quetzi.bluepower.tileentities.tier2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.quetzi.bluepower.api.tube.IPneumaticTube.TubeColor;
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
    public void onButtonPress(int messageId, int value) {
    
        if (messageId < 8) {
            colors[messageId] = TubeColor.values()[value];
        } else if (messageId == 9) {
            pullMode = PullMode.values()[value];
        } else {
            sortMode = SortMode.values()[value];
        }
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
