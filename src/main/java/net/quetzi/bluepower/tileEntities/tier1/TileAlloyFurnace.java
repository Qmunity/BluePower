package net.quetzi.bluepower.tileEntities.tier1;

import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.tileEntities.TileBase;

public class TileAlloyFurnace extends TileBase {
	
	/*************** BASIC TE FUNCTIONS **************/
	
	/**
	 * Function gets called every tick.
	 * Do not forget to call the super method!
	 */
	@Override
	public void updateEntity(){
		super.updateEntity();
	}
	
	/**
	 * This function gets called whenever the world/chunk loads
	 */
	@Override
	public void readFromNBT(NBTTagCompound tCompound){
		super.readFromNBT(tCompound);
	}
	
	/**
	 * This function gets called whenever the world/chunk is saved
	 */
	@Override
	public void writeToNBT(NBTTagCompound tCompound){
		super.writeToNBT(tCompound);
	}
	
}
