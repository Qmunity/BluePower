package com.bluepowermod.tileentities.tier1;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.tileentities.IEjectAnimator;
import com.bluepowermod.tileentities.TileBase;

public class TileDeployer extends TileBase implements ISidedInventory, IEjectAnimator {
    
    public boolean              isActive   = false;
    private ForgeDirection      orientation;
    private boolean             redstoneSignal;
    protected static FakePlayer fakeplayer = null;
    
    @Override
    public void updateEntity() {
    
        isActive = worldObj.isDaytime();
        
    }
    
    private final ItemStack[] allInventories = new ItemStack[9];
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            allInventories[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            if (allInventories[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                allInventories[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
    
        return allInventories.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return allInventories[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
    
        // this needs to be side aware as well
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
    
        return getStackInSlot(i);
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        allInventories[i] = itemStack;
    }
    
    @Override
    public String getInventoryName() {
    
        return "tile.buffer.name";
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return true;
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
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (stack != null) drops.add(stack);
        return drops;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
    
        ForgeDirection direction = getFacingDirection();
        
        if (var1 == direction.ordinal()) { return new int[] {}; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }
    
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean isEjecting() {
    
        return isActive;
    }
    
}
