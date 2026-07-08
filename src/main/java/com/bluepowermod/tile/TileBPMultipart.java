/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import com.bluepowermod.api.multipart.IBPMultipartTile;
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.multipart.IBPPartTile;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.tier1.TileWire;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.data.MultipartModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MoreThanHidden
 */
public class TileBPMultipart extends BlockEntity implements IBPMultipartTile {

    public static final ModelProperty<Map<BlockState, ModelData>> STATE_INFO = new ModelProperty<>();
    public static final ModelProperty<BlockAndTintGetter> LEVEL = new ModelProperty<>();
    public static final ModelProperty<BlockPos> POS = new ModelProperty<>();
    private Map<BlockState, BlockEntity> stateMap = new HashMap<>();
    private EnumMap<Direction, BlockState> statesByDirection = new EnumMap<>(Direction.class);
    VoxelShape shape = null;
    VoxelShape collisionShape = null;

    public TileBPMultipart(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.MULTIPART.get(), pos, state);
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        //Get Model Data for States with Tile Entities in the Multipart
        Map<BlockState, ModelData> modelDataMap = stateMap.keySet().stream().filter(BlockState::hasBlockEntity)
                .collect(Collectors.toMap(s -> s, this::getModelData));

        //Add States without Tile Entities
        stateMap.keySet().stream().filter(s -> !s.hasBlockEntity()).forEach(s -> modelDataMap.put(s, null));

        return ModelData.builder().with(STATE_INFO, modelDataMap).with(LEVEL, this.level).with(POS, this.worldPosition).build();
    }

    private ModelData getModelData(BlockState state) {
        //Get Model Data for specific state
        BlockEntity tileEntity = stateMap.get(state);
        if(tileEntity != null) {
            if (tileEntity instanceof TileWire tileWire)
                return tileWire.getModelData(state);
            return tileEntity.getModelData();
        }
        return ModelData.EMPTY;
    }

    public void addState(BlockState state) {
        BlockEntity tile = null;
        if (state.getBlock() instanceof EntityBlock entityBlock){
            tile = entityBlock.newBlockEntity(worldPosition, state);
            if (tile != null) {
                tile.setLevel(level);
                if (tile instanceof IBPPartTile part){
                    part.setMultipartTile(this);
                }
            }
        }
        addStateToEnumMap(state);
        this.stateMap.put(state, tile);
        state.getBlock().setPlacedBy(level, worldPosition, state,  null, new ItemStack(state.getBlock()));
        shape = null;
        collisionShape = null;
        markDirtyClient();
    }

    private void addStateToEnumMap(BlockState state){
        if (state.hasProperty(BlockStateProperties.FACING)){
            statesByDirection.put(state.getValue(BlockStateProperties.FACING), state);
        } else if (state.getBlock() instanceof WallTorchBlock && state.hasProperty(HorizontalDirectionalBlock.FACING)){
            statesByDirection.put(state.getValue(HorizontalDirectionalBlock.FACING), state);
        } else if (state.getBlock() == Blocks.LEVER){
            var face = state.getValue(LeverBlock.FACE);
            Direction side = state.getValue(LeverBlock.FACING);
            if (face == AttachFace.CEILING){
                side = Direction.DOWN;
            } else if (face == AttachFace.FLOOR){
                side = Direction.UP;
            }
            statesByDirection.put(side, state);
        }
    }

    @Nullable
    public BlockState getStateByFacing(Direction face){
        return statesByDirection.get(face);
    }

    public void removeState(BlockState state) {
        //Drop Items
        if (level instanceof ServerLevel) {
            NonNullList<ItemStack> drops = NonNullList.create();
            drops.addAll(Block.getDrops(state, (ServerLevel) level, worldPosition, this));
            Containers.dropContents(level,worldPosition, drops);
        }
        //Remove Tile Entity
        if(stateMap.get(state) != null) {
            stateMap.get(state).setRemoved();
            if (stateMap.get(state) instanceof IBPPartTile part) part.setMultipartTile(null);
        }
        Direction toRemove = null;
        for (var s : statesByDirection.entrySet()){
            if (s.getValue() == state){
                toRemove = s.getKey();
                break;
            }
        }
        if (toRemove != null) statesByDirection.remove(toRemove);
        //Remove State
        this.stateMap.remove(state);
        shape = null;
        collisionShape = null;
        markDirtyClient();
        if(stateMap.size() == 1) {
            //Convert back to Standalone Block
            BlockEntity te = (BlockEntity)stateMap.values().toArray()[0];
            if (level != null) {
                CompoundTag nbt = te != null ? te.saveWithoutMetadata() : null;
                level.setBlockAndUpdate(worldPosition, ((BlockState)stateMap.keySet().toArray()[0]));
                BlockEntity tile = level.getBlockEntity(worldPosition);
                if (tile != null && nbt != null)
                    tile.load(nbt);
            }
        }else if(stateMap.isEmpty()){
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
    public void setLevel(Level levelIn) {
        super.setLevel(levelIn);
        stateMap.values().stream().filter(Objects::nonNull).forEach(t -> t.setLevel(levelIn));
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

    private void recalculateShape(boolean collision){
        if (collision) {
            collisionShape = Shapes.empty();
        } else {
            shape = Block.box(6,6,6,10,10,10);
        }
        List<VoxelShape> shapeList = new ArrayList<>();
        if (level != null) {
            getStates().forEach(s -> shapeList.add(collision ? s.getCollisionShape(this.level, this.worldPosition) : s.getShape(this.level, this.worldPosition)));
        }

        if(!shapeList.isEmpty()) {
            if (collision) {
                collisionShape = shapeList.stream().reduce(shapeList.get(0), Shapes::or);
            } else {
                shape = shapeList.stream().reduce(shapeList.get(0), Shapes::or);
            }
        }
    }

    public VoxelShape getShape() {
        if (shape == null) {
            recalculateShape(false);
        }
        return shape;
    }

    public VoxelShape getCollisionShape() {
        if (collisionShape == null) {
            recalculateShape(true);
        }
        return collisionShape;
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
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("size", getStates().size());
        for (int i = 0; i < getStates().size(); i++) {
            //write state data
            String stateSave = "state" + i;
            BlockState.CODEC.encodeStart(NbtOps.INSTANCE,  getStates().get(i)).result().ifPresent(nbt -> compound.put(stateSave, nbt));
            //write tile NBT data
            if(stateMap.get(getStates().get(i)) != null)
                compound.put("tile" + i, stateMap.get(getStates().get(i)).saveWithoutMetadata());
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        Map<BlockState, BlockEntity> states = new HashMap<>();
        int size = compound.getInt("size");
        for (int i = 0; i < size; i++) {
            Optional<Pair<BlockState, Tag>> result = BlockState.CODEC.decode(new Dynamic<>(NbtOps.INSTANCE, compound.get("state" + i))).result();
            if(result.isPresent()){
                BlockState state = result.get().getFirst();
                BlockEntity tile = null;
                if (state.getBlock() instanceof EntityBlock entityBlock){
                    tile = entityBlock.newBlockEntity(worldPosition, state);
                    if (tile != null) {
                        if (this.level != null) {
                            tile.setLevel(this.level);
                        }
                        tile.load(compound.getCompound("tile" + i));
                    }

                }
                states.put(state, tile);
            }
        }
        this.stateMap = states;
        shape = null;
        collisionShape = null;
        markDirtyClient();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet) {
        List<BlockState> states = getStates();
        CompoundTag tagCompound = packet.getTag();
        super.onDataPacket(networkManager, packet);
        load(tagCompound);
        if (level.isClientSide) {
            // Update if needed
            if (!getStates().equals(states)) {
                shape = null;
                collisionShape = null;
                level.blockEntityChanged(getBlockPos());
            }
        }
    }

    public void changeState(BlockState state, BlockState newState) {
        BlockEntity te = stateMap.get(state);
        Direction toRemove = null;
        for (var s : statesByDirection.entrySet()){
            if (s.getValue() == state){
                toRemove = s.getKey();
                break;
            }
        }
        if (toRemove != null) statesByDirection.remove(toRemove);
        stateMap.remove(state);
        addStateToEnumMap(newState);
        stateMap.put(newState, te);
        if (te != null) te.setBlockState(newState);
        shape = null;
        collisionShape = null;
        markDirtyClient();
    }

    public static void tickMultipart(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TileBPMultipart multipart = (TileBPMultipart) blockEntity;
        //Tick the Tickable Multiparts
        multipart.stateMap.values().stream().filter(t -> t instanceof TickingBlockEntity)
              .forEach(t-> ((TickingBlockEntity)t).tick());
    }
}
