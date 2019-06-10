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
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBattery extends TileMachineBase {

    private final BlutricityStorage storage = new BlutricityStorage(3200);

    @Override
    public void update() {
        double voltage = storage.getVoltage();
        int level = (int)((voltage / storage.getMaxVoltage()) * 6);
        if(world.getBlockState(pos).getValue(BlockBattery.LEVEL) != level){
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos).withProperty(BlockBattery.LEVEL, level), 0);
            markDirty();
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
            return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(storage);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void readFromPacketNBT(CompoundNBT tCompound) {
        super.readFromPacketNBT(tCompound);
        if(tCompound.hasKey("energy")) {
        NBTBase nbtstorage = tCompound.getTag("energy");
        CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(CompoundNBT tCompound) {
        super.writeToPacketNBT(tCompound);
            NBTBase nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
            tCompound.setTag("energy", nbtstorage);
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

}
