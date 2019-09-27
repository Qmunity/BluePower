/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class TileBPMultipart extends TileEntity {

    public static final ModelProperty<List<BlockState>> PROPERTY_INFO = new ModelProperty<>();
    private List<BlockState> state = new ArrayList<>();

    public TileBPMultipart() {
        super(BPTileEntityType.MULTIPART);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(PROPERTY_INFO, state).build();
    }

    public void addState(BlockState state) {
        this.state.add(state);
        markDirtyClient();
    }

    public void removeState(BlockState state) {
        this.state.remove(state);
        markDirtyClient();
    }

    public List<BlockState> getStates() {
        return state;
    }

    public void setStates(List<BlockState> state) {
        this.state = state;
    }

    private void markDirtyClient() {
        markDirty();
        if (getWorld() != null) {
            BlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
        this.requestModelDataUpdate();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("size", getStates().size());
        for (int i = 0; i < getStates().size(); i++) {
            compound.putString("state" + i, getStates().get(i).getBlock().getRegistryName().toString());
        }
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        List<BlockState> states = new ArrayList<>();
        int size = compound.getInt("size");
        for (int i = 0; i < size; i++) {
            states.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("state" + i))).getDefaultState());
        }
        setStates(states);
        markDirtyClient();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        write(updateTag);
        return updateTag;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        List<BlockState> states = getStates();
        CompoundNBT tagCompound = packet.getNbtCompound();
        super.onDataPacket(networkManager, packet);
        read(tagCompound);
        if (world.isRemote) {
            // Update if needed
            if (!getStates().equals(states)) {
                world.markChunkDirty(getPos(), this.getTileEntity());
            }
        }
    }

}
