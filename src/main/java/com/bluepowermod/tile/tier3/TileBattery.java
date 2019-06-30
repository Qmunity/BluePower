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
import com.bluepowermod.block.power.BlockBattery;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBattery extends TileMachineBase {

    private final BlutricityStorage storage = new BlutricityStorage(3200);

    public TileBattery() {
        super(BPTileEntityType.BATTERY);
    }

    @Override
    public void tick() {
        double voltage = storage.getVoltage();
        int level = (int)((voltage / storage.getMaxVoltage()) * 6);
        if(world.getBlockState(pos).get(BlockBattery.LEVEL) != level){
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos).with(BlockBattery.LEVEL, level), 0);
            markDirty();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        //if(capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
            //return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(storage);
        //}
        return super.getCapability(capability);
    }

    @Override
    protected void readFromPacketNBT(CompoundNBT tCompound) {
        super.readFromPacketNBT(tCompound);
        if(tCompound.contains("energy")) {
        INBT nbtstorage = tCompound.get("energy");
        CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(CompoundNBT tCompound) {
        super.writeToPacketNBT(tCompound);
            INBT nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
            tCompound.put("energy", nbtstorage);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

}
