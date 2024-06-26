/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

/**
 * @author MoreThanHidden
 */
public class TileBPMicroblock extends BlockEntity {

    public static final ModelProperty<Pair<Block, Integer>> PROPERTY_INFO = new ModelProperty<>();
    private Block block = Blocks.STONE;
    private Integer rotation = 0;

    public TileBPMicroblock(BlockPos pos, BlockState state){
        super(BPBlockEntityType.MICROBLOCK.get(), pos, state);
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(PROPERTY_INFO, new ImmutablePair<>(block, rotation)).build();
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
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putString("block", BuiltInRegistries.BLOCK.getKey(block).toString());
        compound.putInt("rotation", rotation);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
       super.loadAdditional(compound, provider);
       if (compound.contains("block")) {
           block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(compound.getString("block")));
           rotation = compound.getInt("rotation");
       }
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag updateTag = super.getUpdateTag(provider);
        saveAdditional(updateTag, provider);
        return updateTag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    
    @Override
    public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider provider) {
        Block oldblock = getBlock();
        CompoundTag tagCompound = packet.getTag();
        super.onDataPacket(networkManager, packet, provider);
        loadAdditional(tagCompound, provider);
        if (level.isClientSide) {
            // Update if needed
            if (!getBlock().equals(oldblock)) {
                level.blockEntityChanged(getBlockPos());
            }
        }
    }

}
