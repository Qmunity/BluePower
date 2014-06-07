package net.quetzi.bluepower.tileEntities.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.quetzi.bluepower.tileEntities.TileBase;

public class TileAlloyFurnace extends TileBase implements IInventory {
	private boolean isActive;
	private boolean metaSet = false;
	private ItemStack[] inventory;
	private ItemStack fuelInventory;
	private ItemStack outputInventory;
	
	public TileAlloyFurnace(){
		inventory = new ItemStack[9];
	}
	
	
	/*************** BASIC TE FUNCTIONS **************/
	
	/**
	 * Function gets called every tick.
	 * Do not forget to call the super method!
	 */
	@Override
	public void updateEntity(){
		super.updateEntity();
		
		//Check if the meta is already set after loading the NBT.
		if(!metaSet){
			metaSet = true;
			if(isActive){
				int newMeta = getBlockMetadata();
				newMeta  = newMeta & 7;
				newMeta |= (isActive == true ? 8 : 0);
				getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
			}
		}
	}
	
	/**
	 * This function gets called whenever the world/chunk loads
	 */
	@Override
	public void readFromNBT(NBTTagCompound tCompound){
		super.readFromNBT(tCompound);
		isActive = tCompound.getBoolean("isActive");
		metaSet = false;
	}
	
	/**
	 * This function gets called whenever the world/chunk is saved
	 */
	@Override
	public void writeToNBT(NBTTagCompound tCompound){
		super.writeToNBT(tCompound);
		tCompound.setBoolean("isActive", isActive);
	}

	
	/**************** ADDED FUNCTIONS **************/
	
	public boolean getIsActive() {
		return isActive;
	}
	
	@Override
	protected void redstoneChanged(boolean newValue) {
		setIsActive(newValue);
	}

	public void setIsActive(boolean _isActive) {
		isActive = _isActive;
		int newMeta = getBlockMetadata();
		newMeta  = newMeta & 7;
		newMeta |= (_isActive == true ? 8 : 0);
		getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);		
	}

	/*************** IINVENTORY *****************/
	
	@Override
	public int getSizeInventory() {
		return 9+1+1; //9 inventory, 1 fuel, 1 output
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		if(var1 == 0){
			return fuelInventory;
		}else if(var1 == 1){
			return outputInventory;
		}else if(var1 < 11){
			return inventory[var1-2];
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return getStackInSlot(var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack itemStack) {
		if(var1 == 0){
			fuelInventory = itemStack;
		}else if(var1 == 1){
			//This shouldn't happen, this is the output slot
		}else{
			inventory[var1-2] = itemStack;
    		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public String getInventoryName() {
		return null;
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
	public boolean isUseableByPlayer(EntityPlayer var1) {
		//Todo: Some fancy code here that detects whether the player is far away
		return true;
	}

	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack itemStack) {
		if(var1 == 0){
			return TileEntityFurnace.isItemFuel(itemStack);
		}else if(var1 == 1){ //Output slot
			return false;
		}else{
			//What items do we want here? Maybe even check the recipes here.
			return true;
		}
		//return false;
	}
	
}
