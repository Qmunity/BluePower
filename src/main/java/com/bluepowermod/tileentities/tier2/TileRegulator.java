package com.bluepowermod.tileentities.tier2;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tileentities.TileMachineBase;

/**
 * @author MineMaarten
 */
public class TileRegulator extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive {
    
    private ItemStack[] inventory = new ItemStack[27];
    public TubeColor    color     = TubeColor.NONE;
    public int          mode;
    
    private enum EnumSection {
        INPUT_FILTER, BUFFER, OUTPUT_FILTER
    }
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        if (!worldObj.isRemote && isBufferEmpty()) {
            boolean ratiosMatch = true;
            for (int i = 0; i < 9; i++) {
                if (inventory[i] != null) {
                    int inputFilterItems = getItemsInSection(inventory[i], EnumSection.INPUT_FILTER);
                    int bufferItems = getItemsInSection(inventory[i], EnumSection.BUFFER);
                    if (bufferItems < inputFilterItems) {
                        ratiosMatch = false;
                        break;
                    }
                }
            }
            if (ratiosMatch && !isEjecting()) checkIndividualOutputFilterAndEject();
            
            if (mode == 1 && !isEjecting()) {//supply mode
                IInventory inv = IOHelper.getInventoryForTE(getTileCache()[getOutputDirection().ordinal()].getTileEntity());
                if (inv != null) {
                    int[] accessibleSlots;
                    if (inv instanceof ISidedInventory) {
                        accessibleSlots = ((ISidedInventory) inv).getAccessibleSlotsFromSide(getFacingDirection().ordinal());
                    } else {
                        accessibleSlots = new int[inv.getSizeInventory()];
                        for (int i = 0; i < accessibleSlots.length; i++)
                            accessibleSlots[i] = i;
                    }
                    for (int i = 18; i < 27; i++) {
                        if (inventory[i] != null) {
                            int outputFilterItems = getItemsInSection(inventory[i], EnumSection.OUTPUT_FILTER);
                            int supplyingInvCount = 0;
                            for (int slot : accessibleSlots) {
                                ItemStack stackInSlot = inv.getStackInSlot(slot);
                                if (stackInSlot != null && stackInSlot.isItemEqual(inventory[i]) && IOHelper.canInsertItemToInventory(inv, inventory[i], slot, getFacingDirection().ordinal())) {
                                    supplyingInvCount += stackInSlot.stackSize;
                                }
                            }
                            if (supplyingInvCount < outputFilterItems) {
                                ItemStack requestedStack = inventory[i].copy();
                                requestedStack.stackSize = outputFilterItems - supplyingInvCount;
                                ItemStack bufferItems = IOHelper.extract(this, ForgeDirection.UNKNOWN, requestedStack, true, false);//try to extract the items needed to fully supply the inventory from the buffer.
                                if (bufferItems != null) {
                                    ItemStack remainder = IOHelper.insert(inv, bufferItems, getFacingDirection().ordinal(), false);//insert into supplying inv.
                                    if (remainder != null) {
                                        IOHelper.insert(this, remainder, ForgeDirection.UNKNOWN, false);//when not every item can be supplied, return those to the buffer.
                                    }
                                }
                            }
                        }
                    }
                }
            }
            boolean shouldEmitRedstone = isSatisfied() || animationTicker >= 0;
            if (isEjecting() != shouldEmitRedstone) {
                setOutputtingRedstone(shouldEmitRedstone);
                sendUpdatePacket();
            }
        }
    }
    
    @Override
    public boolean isEjecting() {
    
        return super.isEjecting() || getOutputtingRedstone() > 0;
    }
    
    /**
     * Returns true if the supplying inventory has the items stated in the output filter.
     * @return
     */
    private boolean isSatisfied() {
    
        IInventory inv = IOHelper.getInventoryForTE(getTileCache()[getOutputDirection().ordinal()].getTileEntity());
        if (inv != null) {
            int[] accessibleSlots;
            if (inv instanceof ISidedInventory) {
                accessibleSlots = ((ISidedInventory) inv).getAccessibleSlotsFromSide(getFacingDirection().ordinal());
            } else {
                accessibleSlots = new int[inv.getSizeInventory()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            boolean everythingNull = true;
            for (int i = 18; i < 27; i++) {
                if (inventory[i] != null) {
                    everythingNull = false;
                    int outputFilterItems = getItemsInSection(inventory[i], EnumSection.OUTPUT_FILTER);
                    int supplyingInvCount = 0;
                    for (int slot : accessibleSlots) {
                        ItemStack stackInSlot = inv.getStackInSlot(slot);
                        if (stackInSlot != null && stackInSlot.isItemEqual(inventory[i]) && IOHelper.canInsertItemToInventory(inv, inventory[i], slot, getFacingDirection().ordinal())) {
                            supplyingInvCount += stackInSlot.stackSize;
                        }
                    }
                    if (supplyingInvCount < outputFilterItems) return false;
                }
            }
            return !everythingNull;
        }
        return false;
    }
    
    private void checkIndividualOutputFilterAndEject() {
    
        //Check in output filter for every slot and look if the items are present in the buffer.
        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                int inputFilterItems = getItemsInSection(inventory[i], EnumSection.INPUT_FILTER);
                int bufferItems = getItemsInSection(inventory[i], EnumSection.BUFFER);
                if (bufferItems >= inputFilterItems) {
                    ItemStack stackFromBuffer = IOHelper.extract(this, ForgeDirection.UNKNOWN, inventory[i], true, false);
                    this.addItemToOutputBuffer(stackFromBuffer, color);
                }
            }
        }
    }
    
    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {
    
        if (from == getFacingDirection() && isBufferEmpty()) {
            stack = stack.copy();
            int bufferItems = getItemsInSection(stack.stack, EnumSection.BUFFER);
            int inputFilterItems = getItemsInSection(stack.stack, EnumSection.INPUT_FILTER);
            int allowedItems = inputFilterItems - bufferItems;
            if (allowedItems <= 0) return stack;
            if (stack.stack.stackSize <= allowedItems) {
                ItemStack remainder = IOHelper.insert(this, stack.stack, ForgeDirection.UNKNOWN.ordinal(), simulate);
                if (remainder == null) return null;
                stack.stack = remainder;
                return stack;
            }
            ItemStack acceptedStack = stack.stack.splitStack(allowedItems);
            ItemStack remainder = IOHelper.insert(this, acceptedStack, ForgeDirection.UNKNOWN.ordinal(), simulate);
            if (remainder != null) {
                stack.stack.stackSize += remainder.stackSize;
            }
            return stack;
        } else {
            return super.acceptItemFromTube(stack, from, simulate);
        }
    }
    
    private int getItemsInSection(ItemStack type, EnumSection section) {
    
        int count = 0;
        for (int i = section.ordinal() * 9; i < section.ordinal() * 9 + 9; i++) {
            if (inventory[i] != null && inventory[i].isItemEqual(type)) count += inventory[i].stackSize;
        }
        return count;
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        if (messageId == 1) {
            mode = value;
        } else if (messageId == 0) {
            color = TubeColor.values()[value];
        }
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (int i = 9; i < 18; i++) {
            if (inventory[i] != null) drops.add(inventory[i]);
        }
        return drops;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        
        tag.setByte("filterColor", (byte) color.ordinal());
        tag.setByte("mode", (byte) mode);
        
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
        
        color = TubeColor.values()[tag.getByte("filterColor")];
        mode = tag.getByte("mode");
        
        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[27];
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
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return inventory[i];
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
    public ItemStack getStackInSlotOnClosing(int i) {
    
        ItemStack itemStack = getStackInSlot(i);
        if (itemStack != null) {
            setInventorySlotContents(i, null);
        }
        return itemStack;
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        inventory[i] = itemStack;
    }
    
    @Override
    public String getInventoryName() {
    
        return BPBlocks.regulator.getUnlocalizedName();
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
    
        if (side == getFacingDirection().ordinal() || side == getOutputDirection().ordinal()) return new int[0];
        int[] slots = new int[9];
        for (int i = 9; i < 18; i++)
            slots[i - 9] = i;
        return slots;
    }
    
    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
    
        return true;
    }
    
    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
    
        return true;
    }
    
}
