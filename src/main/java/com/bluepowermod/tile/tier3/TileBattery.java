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
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.network.Connection;
import net.minecraft.network.play.server.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBattery extends TileMachineBase {
    private final int MAX_ENERGY = 3200;
    private final BlutricityStorage storage = new BlutricityStorage(MAX_ENERGY, 100);
    private LazyOptional<IPowerBase> blutricityCap;

    public TileBattery() {
        super(BPBlockEntityType.BATTERY);
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            storage.resetCurrent();

            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                BlockEntity tile = level.getBlockEntity(worldPosition.relative(facing));
                if (tile != null)
                    tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                            exStorage -> EnergyHelper.balancePower(exStorage, storage));
            }

            double energy = storage.getEnergy();
            int batteryLevel = (int) ((energy / MAX_ENERGY) * 6);
            BlockState state = getBlockState();
            if (state.getValue(BlockBattery.LEVEL) != batteryLevel) {
                this.level.setBlockAndUpdate(worldPosition, state.setValue(BlockBattery.LEVEL, batteryLevel));
                markForRenderUpdate();
                setChanged();
            }
        }
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
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null )
        {
            blutricityCap.invalidate();
            blutricityCap = null;
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
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(getBlockState(), pkt.getTag());
    }

}
