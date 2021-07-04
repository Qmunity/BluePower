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
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.block.power.BlockEngine;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 
 * @author TheFjong, MoreThanHidden
 *
 */
public class TileEngine extends TileMachineBase  {

	private Direction orientation = Direction.DOWN;
	public boolean isActive = false;
    public byte pumpTick;
    public byte pumpSpeed;

	private final BlutricityFEStorage storage = new BlutricityFEStorage(320){
		@Override
		public boolean canReceive() {
			return false;
		}
	};
	private LazyOptional<BlutricityFEStorage> blutricityCap;

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY || cap == CapabilityEnergy.ENERGY){
			if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
			return blutricityCap.cast();
		}
		return LazyOptional.empty();
	}

	
	public TileEngine(){
		super(BPTileEntityType.ENGINE);

		pumpTick  = 0;
		pumpSpeed = 16;
		
	}

    @Override
	public void tick() {
		super.tick();

		storage.resetCurrent();

		//Server side capability check
		isActive = false;
		if(level != null && !level.isClientSide && (storage.getEnergyStored() > 0 && level.hasNeighborSignal(worldPosition))){
			Direction facing = getBlockState().getValue(BlockEngine.FACING).getOpposite();
			TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(facing));
			if (tileEntity != null) {
				tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(other -> {
					int simulated = storage.extractEnergy(320, true);
					int sent = other.receiveEnergy(simulated, false);
					int amount = storage.extractEnergy(sent, false);
					if(amount > 0) {
						isActive = true;
					}
				});
			}
		}

		//Update BlockState
		if(level != null && !level.isClientSide && getBlockState().getValue(BlockEngine.ACTIVE) != isActive){
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockEngine.ACTIVE, isActive));
			markForRenderUpdate();
		}

		//Update TESR from BlockState
		if(level != null && getBlockState().getValue(BlockEngine.ACTIVE)) {
			isActive = true;
			pumpTick++;
			if (pumpTick >= pumpSpeed * 2) {
				pumpTick = 0;
				if (pumpSpeed > 4) {
					pumpSpeed--;
				}
			}
		}else{
			isActive = false;
			pumpTick = 0;
		}

	}

    public void setOrientation(Direction orientation){
        this.orientation = orientation;
        setChanged();
    }

    public Direction getOrientation()
    {
        return orientation;
    }


	@Override
	protected void writeToPacketNBT(CompoundNBT compound) {
		super.writeToPacketNBT(compound);
		int rotation = orientation.get3DDataValue();
		compound.putInt("rotation", rotation);
        compound.putByte("pumpspeed", pumpSpeed);
        compound.putByte("pumptick", pumpTick);
        INBT nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
		compound.put("energy", nbtstorage);

	}

	@Override
	protected void readFromPacketNBT(CompoundNBT compound) {
		super.readFromPacketNBT(compound);
		orientation = Direction.from3DDataValue(compound.getInt("rotation"));
        pumpSpeed = compound.getByte("pumpspeed");
        pumpTick = compound.getByte("pumptick");
        if(compound.contains("energy")) {
            INBT nbtstorage = compound.get("energy");
            CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
	}

	@Override
	protected void invalidateCaps(){
		super.invalidateCaps();
		if( blutricityCap != null )
		{
			blutricityCap.invalidate();
			blutricityCap = null;
		}
	}

}
