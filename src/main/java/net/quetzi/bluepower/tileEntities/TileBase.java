package net.quetzi.bluepower.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBase extends TileEntity {
	private ForgeDirection facing;
	
	
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
		facing = ForgeDirection.getOrientation(tCompound.getInteger("facing"));
	}
	
	/**
	 * This function gets called whenever the world/chunk is saved
	 */
	@Override
	public void writeToNBT(NBTTagCompound tCompound){
		super.writeToNBT(tCompound);
		tCompound.setInteger("facing", facing.ordinal());
	}
	
	
	/***************** ADDED FUNCTIONS *****************/
	
	/**
	 * Method to set the direction this TE is facing.
	 * @param _facing
	 */
	public void setFacing(ForgeDirection _facing){
		facing = _facing;
		//Todo: Sent packet to clients
	}
	
	/**
	 * Method to get the direction this TE is facing.
	 * @return
	 */
	public ForgeDirection getFacing(){
		return facing;
	}
	
}
