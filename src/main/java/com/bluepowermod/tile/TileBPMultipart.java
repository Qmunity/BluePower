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
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
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
public class TileBPMultipart extends TileEntity implements ITickableTileEntity {

    public static final ModelProperty<Map<BlockState, IModelData>> STATE_INFO = new ModelProperty<>();
    private Map<BlockState, TileEntity> stateMap = new HashMap<>();

    public TileBPMultipart() {
        super(BPTileEntityType.MULTIPART);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        //Get Model Data for States with Tile Entities in the Multipart
        Map<BlockState, IModelData> modelDataMap = stateMap.keySet().stream().filter(IForgeBlockState::hasTileEntity)
                .collect(Collectors.toMap(s -> s, this::getModelData));

        //Add States without Tile Entities
        stateMap.keySet().stream().filter(s -> !s.hasTileEntity()).forEach(s -> modelDataMap.put(s, null));

        return new ModelDataMap.Builder().withInitial(STATE_INFO, modelDataMap).build();
    }

    private IModelData getModelData(BlockState state) {
        //Get Model Data for specific state
        TileEntity tileEntity = stateMap.get(state);
        if(tileEntity != null) {
            if (tileEntity instanceof TileWire)
                return ((TileWire) tileEntity).getModelData(state);
            return tileEntity.getModelData();
        }
        return EmptyModelData.INSTANCE;
    }

    public void addState(BlockState state) {
        TileEntity tile = state.getBlock().createTileEntity(state, world);
        if (tile != null) {
            tile.setPos(pos);
        }
        this.stateMap.put(state, tile);
        state.getBlock().onBlockPlacedBy(world, pos, state,  null, new ItemStack(state.getBlock()));
        markDirtyClient();
    }

    public void removeState(BlockState state) {
        //Drop Items
        if (world instanceof ServerWorld) {
            NonNullList<ItemStack> drops = NonNullList.create();
            drops.addAll(Block.getDrops(state, (ServerWorld) world,  pos, this));
            InventoryHelper.dropItems(world, pos, drops);
        }
        //Remove Tile Entity
        if(stateMap.get(state) != null) {
            stateMap.get(state).remove();
        }
        //Remove State
        this.stateMap.remove(state);
        markDirtyClient();
        if(stateMap.size() == 1) {
            //Convert back to Standalone Block
            TileEntity te = (TileEntity)stateMap.values().toArray()[0];
            if (world != null) {
                CompoundNBT nbt = te != null ? te.write(new CompoundNBT()) : null;
                world.setBlockState(pos, ((BlockState)stateMap.keySet().toArray()[0]));
                TileEntity tile = world.getTileEntity(pos);
                if (tile != null && nbt != null)
                    tile.read(getBlockState(), nbt);
            }
        }else if(stateMap.size() == 0){
            //Remove if this is empty
            if (world != null) {
                world.removeBlock(pos, false);
            }
        }
        if(world != null)
            world.getBlockState(pos).neighborChanged(world, pos, getBlockState().getBlock(), pos, false);
    }

    public TileEntity getTileForState(BlockState state){
        return stateMap.get(state);
    }

    @Override
    public void setWorldAndPos(World worldIn, BlockPos posIn) {
        super.setWorldAndPos(worldIn, posIn);
        stateMap.values().forEach(t -> t.setWorldAndPos(worldIn, posIn));
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
            //write state data
            String stateSave = "state" + i;
            BlockState.CODEC.encodeStart(NBTDynamicOps.INSTANCE,  getStates().get(i)).result().ifPresent(nbt -> compound.put(stateSave, nbt));
            //write tile NBT data
            if(stateMap.get(getStates().get(i)) != null)
                compound.put("tile" + i, stateMap.get(getStates().get(i)).write(new CompoundNBT()));
        }
        return compound;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        Map<BlockState, TileEntity> states = new HashMap<>();
        int size = compound.getInt("size");
        for (int i = 0; i < size; i++) {
            Optional<Pair<BlockState, INBT>> result = BlockState.CODEC.decode(new Dynamic<>(NBTDynamicOps.INSTANCE, compound.get("state" + i))).result();
            if(result.isPresent()){
                BlockState state = result.get().getFirst();
                TileEntity tile = state.getBlock().createTileEntity(state, getWorld());
                if (tile != null) {
                    tile.read(state, compound.getCompound("tile" + i));
                    tile.setPos(pos);
                }
                states.put(state, tile);
            }
        }
        this.stateMap = states;
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
        read(getBlockState(), tagCompound);
        if (world.isRemote) {
            // Update if needed
            if (!getStates().equals(states)) {
                world.markChunkDirty(getPos(), this.getTileEntity());
            }
        }
    }

    public void changeState(BlockState state, BlockState newState) {
        TileEntity te = stateMap.get(state);
        stateMap.remove(state);
        stateMap.put(newState, te);
        markDirtyClient();
    }

    @Override
    public void tick() {
      //Tick the Tickable Multiparts
      stateMap.values().stream().filter(t -> t instanceof ITickableTileEntity)
              .forEach(t-> ((ITickableTileEntity)t).tick());
    }
}
