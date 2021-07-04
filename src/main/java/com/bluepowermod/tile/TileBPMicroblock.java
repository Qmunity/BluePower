/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

/**
 * @author MoreThanHidden
 */
public class TileBPMicroblock extends TileEntity {

    public static final ModelProperty<Pair<Block, Integer>> PROPERTY_INFO = new ModelProperty<>();
    private Block block = Blocks.STONE;
    private Integer rotation = 0;

    public TileBPMicroblock(){
        super(BPTileEntityType.MICROBLOCK);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(PROPERTY_INFO, new ImmutablePair<>(block, rotation)).build();
    }

    public void setBlock(Block block) {
        this.block = block;
        this.requestModelDataUpdate();
        markDirtyClient();
    }

    public Block getBlock() {
        return block;
    }

    private void markDirtyClient() {
        setChanged();
        if (getLevel() != null) {
            BlockState state = getLevel().getBlockState(getBlockPos());
            getLevel().sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putString("block", block.getRegistryName().toString());
        compound.putInt("rotation", rotation);
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
       super.load(state, compound);
       block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("block")));
       rotation = compound.getInt("block");
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = super.getUpdateTag();
        save(updateTag);
        return updateTag;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        save(nbtTag);
        return new SUpdateTileEntityPacket(getBlockPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        Block oldblock = getBlock();
        CompoundNBT tagCompound = packet.getTag();
        super.onDataPacket(networkManager, packet);
        load(getBlockState(), tagCompound);
        if (level.isClientSide) {
            // Update if needed
            if (!getBlock().equals(oldblock)) {
                level.blockEntityChanged(getBlockPos(), this.getTileEntity());
            }
        }
    }

}
