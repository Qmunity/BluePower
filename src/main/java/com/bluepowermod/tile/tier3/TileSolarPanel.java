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
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;


/**
 * 
 * @author MoreThanHidden
 *
 */
public class TileSolarPanel extends TileMachineBase  {


	private final IPowerBase storage = new BlutricityStorage(10);

	@Override
	public void update() {
		if(world.isDaytime() && world.canSeeSky(pos) && storage.getVoltage() < 10)
			storage.addEnergy(-(storage.getVoltage() - 10), false);

		for (Direction facing : Direction.values()){
		    TileEntity tile = world.getTileEntity(pos.offset(facing));
		    if((storage.getVoltage() > 0) && tile != null && tile.hasCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite())){
		        IPowerBase exStorage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite());
		        if(exStorage.getVoltage() < exStorage.getMaxVoltage()) {
					storage.addEnergy(-(exStorage.addEnergy(storage.getVoltage(), false)), false);
				}
            }
        }
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
		if(capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
				return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(this.storage);
		}
		return super.getCapability(capability, facing);
	}
}
