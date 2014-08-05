package com.bluepowermod.tileentities.tier3;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.tileentities.TileBase;

public class TileKinectGenerator extends TileBase implements ISidedInventory{

	public int windspeed = 10;
	public int windtick = 0;
	public TileKinectGenerator(){
		
		
		
	}
	
	
	
	@Override
	public void updateEntity() {
		
        if (windspeed < 0){
			windtick +=windspeed;
		}
	}
    private final ItemStack[] allInventories = new ItemStack[1];

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);

        for (int i = 0; i < 1; i++) {
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

        for (int i = 0; i < 1; i++) {
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

        return this.allInventories[i];
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

        this.allInventories[i] = itemStack;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String getInventoryName() {

        return "tile.kinect.name";
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
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
  
        return false;
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return null;
	}
}
