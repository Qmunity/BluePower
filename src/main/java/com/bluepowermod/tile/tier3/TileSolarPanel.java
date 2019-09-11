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
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;


/**
 * 
 * @author MoreThanHidden
 *
 */
public class TileSolarPanel extends TileMachineBase  {


	private final IPowerBase storage = new BlutricityStorage(10);
	private LazyOptional<IPowerBase> blutricityCap;

	public TileSolarPanel() {
		super(BPTileEntityType.SOLAR_PANEL);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
			if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
			return blutricityCap.cast();
		}
		return super.getCapability(cap);
	}


	@Override
	public void tick() {
		if(world.isDaytime() && world.canBlockSeeSky(pos) && storage.getVoltage() < 10)
			storage.addEnergy(1 + new Double("0." + new Random().nextInt(3)), false);

		for (Direction facing : Direction.values()){
		    TileEntity tile = world.getTileEntity(pos.offset(facing));
		    if((storage.getVoltage() > 0) && tile != null && tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).isPresent()){
		        IPowerBase exStorage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).orElse(null);
		        if(exStorage.getVoltage() < exStorage.getMaxVoltage()) {
					storage.addEnergy(-(exStorage.addEnergy(storage.getVoltage(), false)), false);
					storage.setCurrent(0.9 + new Double("0." + new Random().nextInt(3)));
				}
            }
        }

		if(storage.getCurrent() != 0 && storage.getVoltage() == storage.getMaxVoltage()){
			storage.setCurrent(0);
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
