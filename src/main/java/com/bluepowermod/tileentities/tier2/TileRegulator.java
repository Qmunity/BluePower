package com.bluepowermod.tileentities.tier2;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.tileentities.TileMachineBase;

public class TileRegulator extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive {
    
    private ItemStack[] inventory = new ItemStack[27];
    public TubeColor    color     = TubeColor.NONE;
    public int          mode;
    
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
    
        return "tile.regulator.name";
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
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
    
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
