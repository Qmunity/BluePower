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
import com.bluepowermod.block.power.BlockBattery;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBattery extends TileMachineBase {
    private final int MAX_ENERGY = 3200;
    private final BlutricityStorage storage = new BlutricityStorage(MAX_ENERGY, 100);

    public TileBattery(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BATTERY.get(), pos, state);
    }

    public static void tickBattery(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!level.isClientSide && blockEntity instanceof TileBattery) {
            TileBattery tileBattery = (TileBattery) blockEntity;
            tileBattery.storage.resetCurrent();

            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                var exStorage = level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, pos.relative(facing), facing.getOpposite());
                if (exStorage != null) {
                    EnergyHelper.balancePower(exStorage, tileBattery.storage);
                }
            }

            double energy = tileBattery.storage.getEnergy();
            int batteryLevel = (int) ((energy / tileBattery.MAX_ENERGY) * 6);
            if (state.getValue(BlockBattery.LEVEL) != batteryLevel) {
                level.setBlockAndUpdate(pos, state.setValue(BlockBattery.LEVEL, batteryLevel));
                tileBattery.markForRenderUpdate();
                tileBattery.setChanged();
            }
        }
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        if(tCompound.contains("energy")) {
        Tag nbtstorage = tCompound.get("energy");
        CapabilityBlutricity.readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
            Tag nbtstorage = CapabilityBlutricity.writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
            tCompound.put("energy", nbtstorage);
    }

}
