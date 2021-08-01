/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.tile.tier1.TileWire;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.inventory.Containers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.Connection;
import net.minecraft.network.play.server.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeBlockState;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MoreThanHidden
 */
public class TileBPMultipart extends BlockEntity implements TickableBlockEntity {

    public static final ModelProperty<Map<BlockState, IModelData>> STATE_INFO = new ModelProperty<>();
    private Map<BlockState, BlockEntity> stateMap = new HashMap<>();

    public TileBPMultipart() {
        super(BPBlockEntityType.MULTIPART);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        //Get Model Data for States with Tile Entities in the Multipart
        Map<BlockState, IModelData> modelDataMap = stateMap.keySet().stream().filter(IForgeBlockState::hasBlockEntity)
                .collect(Collectors.toMap(s -> s, this::getModelData));

        //Add States without Tile Entities
        stateMap.keySet().stream().filter(s -> !s.hasBlockEntity()).forEach(s -> modelDataMap.put(s, null));

        return new ModelDataMap.Builder().withInitial(STATE_INFO, modelDataMap).build();
    }

    private IModelData getModelData(BlockState state) {
        //Get Model Data for specific state
        BlockEntity tileEntity = stateMap.get(state);
        if(tileEntity != null) {
            if (tileEntity instanceof TileWire)
                return ((TileWire) tileEntity).getModelData(state);
            return tileEntity.getModelData();
        }
        return EmptyModelData.INSTANCE;
    }

    public void addState(BlockState state) {
        BlockEntity tile = state.getBlock().createBlockEntity(state, level);
        if (tile != null) {
            tile.setPosition(worldPosition);
        }
        this.stateMap.put(state, tile);
        state.getBlock().setPlacedBy(level, worldPosition, state,  null, new ItemStack(state.getBlock()));
        markDirtyClient();
    }

    public void removeState(BlockState state) {
        //Drop Items
        if (level instanceof ServerWorld) {
            NonNullList<ItemStack> drops = NonNullList.create();
            drops.addAll(Block.getDrops(state, (ServerWorld) level, worldPosition, this));
            Containers.dropContents(level,worldPosition, drops);
        }
        //Remove Tile Entity
        if(stateMap.get(state) != null) {
            stateMap.get(state).setRemoved();
        }
        //Remove State
        this.stateMap.remove(state);
        markDirtyClient();
        if(stateMap.size() == 1) {
            //Convert back to Standalone Block
            BlockEntity te = (BlockEntity)stateMap.values().toArray()[0];
            if (level != null) {
                CompoundTag nbt = te != null ? te.save(new CompoundTag()) : null;
                level.setBlockAndUpdate(worldPosition, ((BlockState)stateMap.keySet().toArray()[0]));
                BlockEntity tile = level.getBlockEntity(worldPosition);
                if (tile != null && nbt != null)
                    tile.load(getBlockState(), nbt);
            }
        }else if(stateMap.size() == 0){
            //Remove if this is empty
            if (level != null) {
                level.removeBlock(worldPosition, false);
            }
        }
        if(level != null)
            level.getBlockState(worldPosition).neighborChanged(level, worldPosition, getBlockState().getBlock(), worldPosition, false);
    }

    public BlockEntity getTileForState(BlockState state){
        return stateMap.get(state);
    }

    @Override
    public void setLevelAndPosition(World levelIn, BlockPos posIn) {
        super.setLevelAndPosition(levelIn, posIn);
        stateMap.values().forEach(t -> t.setLevelAndPosition(levelIn, posIn));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //If any of the states are blocking the given side return empty.
        if(isSideBlocked(cap, side)){
            return LazyOptional.empty();
        }
        //Get Matching Capabilities from the contained Tile Entities.
        List<LazyOptional<T>> capability =  stateMap.values().stream().filter(Objects::nonNull)
                .map(t -> t.getCapability(cap, side)).filter(LazyOptional::isPresent).collect(Collectors.toList());
        return capability.size() > 0 ? capability.get(0) : LazyOptional.empty();
    }

    public Boolean isSideBlocked(@Nonnull Capability cap, @Nullable Direction side){
        return stateMap.keySet().stream().filter(s -> s.getBlock() instanceof IBPPartBlock)
                .anyMatch(s -> ((IBPPartBlock)s.getBlock()).blockCapability(s, cap, side));
    }

    public List<BlockState> getStates(){
        return new ArrayList<>(stateMap.keySet());
    }

    private void markDirtyClient() {
        setChanged();
        if (getLevel() != null) {
            BlockState state = getLevel().getBlockState(getBlockPos());
            getLevel().sendBlockUpdated(getBlockPos(), state, state, 3);
        }
        this.requestModelDataUpdate();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("size", getStates().size());
        for (int i = 0; i < getStates().size(); i++) {
            //write state data
            String stateSave = "state" + i;
            BlockState.CODEC.encodeStart(NBTDynamicOps.INSTANCE,  getStates().get(i)).result().ifPresent(nbt -> compound.put(stateSave, nbt));
            //write tile NBT data
            if(stateMap.get(getStates().get(i)) != null)
                compound.put("tile" + i, stateMap.get(getStates().get(i)).save(new CompoundTag()));
        }
        return compound;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        Map<BlockState, BlockEntity> states = new HashMap<>();
        int size = compound.getInt("size");
        for (int i = 0; i < size; i++) {
            Optional<Pair<BlockState, INBT>> result = BlockState.CODEC.decode(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("state" + i))).result();
            if(result.isPresent()){
                BlockState state = result.get().getFirst();
                BlockEntity tile = state.getBlock().createBlockEntity(state, getLevel());
                if (tile != null) {
                    tile.load(state, compound.getCompound("tile" + i));
                    tile.setPosition(worldPosition);
                }
                states.put(state, tile);
            }
        }
        this.stateMap = states;
        markDirtyClient();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        save(updateTag);
        return updateTag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbtTag = new CompoundTag();
        save(nbtTag);
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet) {
        List<BlockState> states = getStates();
        CompoundTag tagCompound = packet.getTag();
        super.onDataPacket(networkManager, packet);
        load(getBlockState(), tagCompound);
        if (level.isClientSide) {
            // Update if needed
            if (!getStates().equals(states)) {
                level.blockEntityChanged(getBlockPos(), this.getBlockEntity());
            }
        }
    }

    public void changeState(BlockState state, BlockState newState) {
        BlockEntity te = stateMap.get(state);
        stateMap.remove(state);
        stateMap.put(newState, te);
        markDirtyClient();
    }

    @Override
    public void tick() {
      //Tick the Tickable Multiparts
      stateMap.values().stream().filter(t -> t instanceof ITickableBlockEntity)
              .forEach(t-> ((ITickableBlockEntity)t).tick());
    }
}
