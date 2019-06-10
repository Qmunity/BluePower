/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityFEStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
;

import com.bluepowermod.tile.TileMachineBase;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * 
 * @author TheFjong, MoreThanHidden
 *
 */
public class TileEngine extends TileMachineBase  {

	private Direction orientation;
	public boolean isActive = false;
    public byte pumpTick;
    public byte pumpSpeed;

	private final BlutricityFEStorage storage = new BlutricityFEStorage(320){
		@Override
		public boolean canReceive() {
			return false;
		}
	};

	
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

		isActive = (storage.getEnergyStored() > 0 && world.isBlockPowered(pos));

	}

    public void setOrientation(Direction orientation){
        this.orientation = orientation;
        markDirty();
    }

    public Direction getOrientation()
    {
        return orientation;
    }


	@Override
	protected void writeToPacketNBT(CompoundNBT compound) {
		super.writeToPacketNBT(compound);
		int rotation = orientation.getIndex();
		compound.setInteger("rotation", rotation);
        compound.setByte("pumpspeed", pumpSpeed);
        compound.setByte("pumptick", pumpTick);
        NBTBase nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
		compound.setTag("energy", nbtstorage);

	}

	@Override
	protected void readFromPacketNBT(CompoundNBT compound) {
		super.readFromPacketNBT(compound);
		orientation = Direction.byIndex(compound.getInteger("rotation"));
        pumpSpeed = compound.getByte("pumpspeed");
        pumpTick = compound.getByte("pumptick");
        if(compound.hasKey("energy")) {
            NBTBase nbtstorage = compound.getTag("energy");
            CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.writeToNBT(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
		if(capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY ) {
			return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(storage);
		}
		return super.getCapability(capability, facing);
    }
}
