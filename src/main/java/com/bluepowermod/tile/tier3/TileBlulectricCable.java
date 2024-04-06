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
import com.bluepowermod.block.power.BlockBlulectricCable;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricCable extends TileMachineBase {
    private final BlutricityStorage storage = new BlutricityStorage(100, 100);

    public TileBlulectricCable(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BLULECTRIC_CABLE.get(), pos, state);
    }

    public static void tickCable(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof TileBlulectricCable) {
            TileBlulectricCable tileCable = (TileBlulectricCable) blockEntity;
            tileCable.storage.resetCurrent();
            if (level != null && !level.isClientSide) {
                if (state.getBlock() instanceof BlockBlulectricCable) {
                    List<Direction> directions = new ArrayList<>(BlockBlulectricCable.FACING.getPossibleValues());

                    //Check the side has capability
                    directions.removeIf(d -> level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, pos, d) == null);

                    //Balance power of attached blulectric blocks.
                    for (Direction facing : directions) {
                        Block fBlock = level.getBlockState(pos.relative(facing)).getBlock();
                        IPowerBase exStorage;
                        if (fBlock != Blocks.AIR && fBlock != Blocks.WATER) {
                            exStorage = level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, pos.relative(facing), facing.getOpposite());
                        } else {
                            exStorage = level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, pos.relative(facing).relative(state.getValue(BlockBlulectricCable.FACING).getOpposite()), facing.getOpposite());
                        }
                        if(exStorage != null){
                            EnergyHelper.balancePower(exStorage, tileCable.storage);
                        }
                    }
                }
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
