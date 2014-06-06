package net.quetzi.bluepower.tileEntities.tier1;

import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.tileEntities.TileBase;

public class TileAlloyFurnace extends TileBase {
	private boolean isActive;
	private boolean metaSet = false;
	
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
	
}
