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
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

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

	
	public TileEngine(){
		
		pumpTick  = 0;
		pumpSpeed = 16;
		
	}

    @Override
	public void update() {
		super.update();
		if(world.isRemote){
			if(isActive){
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

		//Todo: Replace with Energy Capability
		isActive = getIsRedstonePowered();

	}

    public void setOrientation(EnumFacing orientation){
        this.orientation = orientation;
        markDirty();
    }

    public EnumFacing getOrientation()
    {
        return orientation;
    }


	@Override
	protected void writeToPacketNBT(NBTTagCompound compound) {
		super.writeToPacketNBT(compound);
		int rotation = orientation.getIndex();
		compound.setInteger("rotation", rotation);
        compound.setByte("pumpspeed", pumpSpeed);
        compound.setByte("pumptick", pumpTick);
	}

	@Override
	protected void readFromPacketNBT(NBTTagCompound compound) {
		super.readFromPacketNBT(compound);
		orientation = EnumFacing.getFront(compound.getInteger("rotation"));
        pumpSpeed = compound.getByte("pumpspeed");
        pumpTick = compound.getByte("pumptick");
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
}
