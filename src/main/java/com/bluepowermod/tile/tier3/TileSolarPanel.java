/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * 
 * @author MoreThanHidden
 *
 */
public class TileSolarPanel extends TileMachineBase  {

	private final int MAX_VOLTAGE = 100;
	private final BlutricityStorage storage = new BlutricityStorage(MAX_VOLTAGE, MAX_VOLTAGE);
	private LazyOptional<IPowerBase> blutricityCap;

	public TileSolarPanel() {
		super(BPBlockEntityType.SOLAR_PANEL);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
			if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
			return blutricityCap.cast();
		}
		return LazyOptional.empty();
	}


	@Override
	public void tick() {
		if (!level.isClientSide) {
			storage.resetCurrent();

			if (level.isDay() && level.canSeeSky(worldPosition) && storage.getEnergy() < MAX_VOLTAGE && !level.isRaining())
				storage.addEnergy(0.2, false);

			//Balance power of attached blulectric blocks.
			for (Direction facing : Direction.values()) {
				BlockEntity tile = level.getBlockEntity(worldPosition.relative(facing));
				if (tile != null)
					tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
							exStorage -> EnergyHelper.balancePower(exStorage, storage));
			}
		}
	}

	@Override
	protected void readFromPacketNBT(CompoundTag tCompound) {
		super.readFromPacketNBT(tCompound);
		if(tCompound.contains("energy")) {
			INBT nbtstorage = tCompound.get("energy");
			CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
		}
	}

	@Override
	protected void writeToPacketNBT(CompoundTag tCompound) {
		super.writeToPacketNBT(tCompound);
		INBT nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
		tCompound.put("energy", nbtstorage);
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
