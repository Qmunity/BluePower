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
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricCable extends TileMachineBase {
    private final BlutricityStorage storage = new BlutricityStorage(100, 100);
    private LazyOptional<IPowerBase> blutricityCap;

    public TileBlulectricCable() {
        super(BPTileEntityType.BLULECTRIC_CABLE);
    }

    @Override
    public void tick() {
        storage.resetCurrent();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockBlulectricCable) {
                List<Direction> directions = new ArrayList<>(BlockBlulectricCable.FACING.getPossibleValues());

                //Check the side has capability
                directions.removeIf(d -> !getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d).isPresent());

                //Balance power of attached blulectric blocks.
                for (Direction facing : directions) {
                    Block fBlock = level.getBlockState(worldPosition.relative(facing)).getBlock();
                    if (fBlock != Blocks.AIR && fBlock != Blocks.WATER) {
                        TileEntity tile = level.getBlockEntity(worldPosition.relative(facing));
                        if (tile != null)
                            tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                                    exStorage -> EnergyHelper.balancePower(exStorage, storage)
                            );
                    } else {
                        TileEntity tile = level.getBlockEntity(worldPosition.relative(facing).relative(state.getValue(BlockBlulectricCable.FACING).getOpposite()));
                        if (tile != null)
                            tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, state.getValue(BlockBlulectricCable.FACING)).ifPresent(
                                    exStorage -> EnergyHelper.balancePower(exStorage, storage)
                            );
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        List<Direction> directions = new ArrayList<>(BlockBlulectricCable.FACING.getPossibleValues());
        if(level != null) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockBlulectricCable) {

                //Remove upward connections
                directions.remove(state.getValue(BlockBlulectricCable.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> level.getBlockState(worldPosition.relative(d)).getBlock() instanceof BlockBlulectricCable
                        && level.getBlockState(worldPosition.relative(d)).getValue(BlockBlulectricCable.FACING) != state.getValue(BlockBlulectricCable.FACING));
            }
        }
        if (cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY && (side == null || directions.contains(side))) {
            if (blutricityCap == null) blutricityCap = LazyOptional.of(() -> storage);
            return blutricityCap.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null ){
            blutricityCap.invalidate();
            blutricityCap = null;
        }
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

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(getBlockState(), pkt.getTag());
    }

}
