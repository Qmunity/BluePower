/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.tile.TileMachineBase;

/**
 * 
 * @author TheFjong
 *
 */
public class TileEngine extends TileMachineBase{

	private EnumFacing orientation;
	public boolean isActive = false;
	public byte pumpTick;
	public byte pumpSpeed;
	public byte gearSpeed;
	public byte gearTick;
	
	public TileEngine(){
		
		pumpTick  = 0;
		pumpSpeed = 16;
		gearSpeed = 16;
		
	}
	
	@Override
	public void update() {
		super.update();
		
		if(world.isRemote){
			if(isActive){
				gearTick++;
				pumpTick++;
				if(pumpTick >= pumpSpeed *2){
					pumpTick = 0;
					if(pumpSpeed > 4){
						pumpSpeed--;
					}
				}
				
			}else{
				pumpTick = 0;
			}
			
		}
		
		
		
		isActive = true;
	}

	public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
    }

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
    
    public void setOrientation(EnumFacing orientation)
    {
        this.orientation = orientation;
    }

    public EnumFacing getOrientation()
    {
        return orientation;
    }
    
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	int rotation = orientation.ordinal();
    	compound.setInteger("rotation", rotation);
//    	compound.setByte("pumpTick", pumpTick);
//    	compound.setByte("pumpSpeed", pumpSpeed);
//    	compound.setByte("gearTick", gearTick);
		return compound;
    	
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    	
    	setOrientation(EnumFacing.fromAngle(compound.getInteger("rotation")));
//    	pumpTick  = compound.getByte("pumpTick");
//    	pumpSpeed =compound.getByte("pumpSpeed");
//    	gearTick =compound.getByte("gearTick");
    }
}
