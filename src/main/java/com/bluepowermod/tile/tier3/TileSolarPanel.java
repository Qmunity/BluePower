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
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * 
 * @author MoreThanHidden
 *
 */
public class TileSolarPanel extends TileMachineBase  {

	private final int MAX_VOLTAGE = 100;
	public final BlutricityStorage storage = new BlutricityStorage(MAX_VOLTAGE, MAX_VOLTAGE);

	public TileSolarPanel(BlockPos pos, BlockState state) {
		super(BPBlockEntityType.SOLAR_PANEL.get(), pos, state);
	}

	public static void tickSolarPanel(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!level.isClientSide && blockEntity instanceof TileSolarPanel) {
			TileSolarPanel tileSolarPanel = (TileSolarPanel) blockEntity;
			tileSolarPanel.storage.resetCurrent();

			if (!level.isDarkOutside() && level.canSeeSky(pos) && tileSolarPanel.storage.getEnergy() < tileSolarPanel.MAX_VOLTAGE && !level.isRaining())
				tileSolarPanel.storage.addEnergy(0.2, false);

			//Balance power of attached blulectric blocks.
			for (Direction facing : Direction.values()) {
                IPowerBase exStorage = level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY,pos.relative(facing), facing.getOpposite());
				if(exStorage != null){
					EnergyHelper.balancePower(exStorage, tileSolarPanel.storage);
				}
			}
		}
	}

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        CapabilityBlutricity.loadEnergy(storage, input);
	}

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
		CapabilityBlutricity.saveEnergy(storage, valueOutput);
	}

}
