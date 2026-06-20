/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.gates;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.block.BlockBase;
import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.TileGate;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;
import java.util.*;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

/**
 * @author MoreThanHidden
 */
public abstract class BlockGateBase extends BlockBase implements SimpleWaterloggedBlock, IBPPartBlock, EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public enum Side {
        BACK,
        FRONT,
        LEFT,
        RIGHT
    }

    public BlockGateBase() {
        super(Properties.of().sound(SoundType.STONE).instabreak());
        this.registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(ROTATION, 0));
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileGate(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, ROTATION, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABBUtils.rotate(Refs.GATE_AABB, state.getValue(FACING));
    }

    public int rotate(int rotation, int steps) {
        return (rotation + steps) % 4;
    }


    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        BlockState oldState = state;
        switch (rotation){
            case NONE -> {}
            case CLOCKWISE_90 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION),  1));
            case CLOCKWISE_180 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION), 2));
            case COUNTERCLOCKWISE_90 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION), 3));
        }
        if (level.getBlockEntity(pos) instanceof TileBPMultipart multipart){
            multipart.changeState(oldState, state);
        } else {
            level.setBlock(pos, state, 3);
        }
        return state;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return state.getValue(FACING) != side && (state.getValue(FACING).getOpposite() != side);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        Direction face = context.getClickedFace();
        return this.defaultBlockState().setValue(ROTATION, DirectionHelper.getRotationFromContext(context)).setValue(FACING, face).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        BlockEntity te = world.getBlockEntity(pos);
        TileGate tileGate = getGateTile(state, te);
        if (tileGate == null) return;
        onBlockPlace(state, tileGate);
        Map<Side, Byte> map = getSidePower(state, tileGate);
        boolean powerChanged = tileGate.updateStates(map, true);
        if (powerChanged) {
            Block blockToTick = te instanceof TileBPMultipart bpMultipart ? bpMultipart.getBlockState().getBlock() : this;
            if (!world.getBlockTicks().willTickThisTick(pos, blockToTick)) {
                world.scheduleTick(pos, blockToTick,
                        getDelay(state, tileGate), TickPriority.HIGH);
            }
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side){
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        Side side1 = fromDirection(side.getOpposite(), blockState.getValue(ROTATION), dirs);
        TileGate tileGate = getGateTile(blockState, blockAccess.getBlockEntity(pos));
        if (tileGate != null && isSideSource(side1, blockState, tileGate)){
            return tileGate.redstoneFromSide(side1);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        Side side1 = fromDirection(side.getOpposite(), blockState.getValue(ROTATION), dirs);
        TileGate tileGate = getGateTile(blockState, blockAccess.getBlockEntity(pos));
        if (tileGate != null && isSideSource(side1, blockState, tileGate)){
            return tileGate.redstoneFromSide(side1);
        }
        return 0;
    }

    protected Direction toDirection(Side side, BlockState state){
        Direction[] dirs = DirectionHelper.ArrayFromDirection(state.getValue(FACING));
        Direction left = dirs[state.getValue(ROTATION) == 3 ? 0 : state.getValue(ROTATION) + 1];
        Direction back = dirs[state.getValue(ROTATION)];
        return switch (side){
            case FRONT -> back.getOpposite();
            case BACK -> back;
            case LEFT -> left;
            case RIGHT -> left.getOpposite();
        };
    }

    protected Side fromDirection(Direction direction, int rotation, Direction[] array){
        Direction sideLeft = array[rotation == 3 ? 0 : rotation + 1];
        Direction sideRight = sideLeft.getOpposite();
        Direction sideBack = array[rotation];
        Direction sideFront = sideBack.getOpposite();
        if (direction == sideFront) return Side.FRONT;
        if (direction == sideBack) return Side.BACK;
        if (direction == sideLeft) return Side.LEFT;
        if (direction == sideRight) return Side.RIGHT;
        return null;
    }

    protected abstract Map<Side, Byte> getSidePower(BlockState state, TileGate gate);

    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate){
        return false;
    }

    protected void onBlockPlace(BlockState state, TileGate gate){

    }

    protected void cycleDisabledStates(BlockState state, TileGate gate){

    }

    public void cycleDisabledStates(BlockState state, BlockGetter blockGetter, BlockPos pos){
        this.cycleDisabledStates(state, getGateTile(state, blockGetter.getBlockEntity(pos)));
    }

    protected int getDelay(BlockState state, TileGate gate){
        return 1;
    }

    protected TileGate getGateTile(BlockState state, BlockEntity inWorldBE){
        BlockEntity gateBE = inWorldBE instanceof TileBPMultipart bpMultipart ? bpMultipart.getTileForState(state) : inWorldBE;
        return gateBE instanceof TileGate gate ? gate : null;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }



    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, level, pos, blockIn, fromPos, bool);
        BlockEntity te = level.getBlockEntity(pos);
        if(!level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isCollisionShapeFullBlock(level,pos.relative(state.getValue(FACING).getOpposite()))) {
            if (te instanceof TileBPMultipart tileBPMultipart) {
                tileBPMultipart.removeState(state);
            } else {
                level.destroyBlock(pos, true);
            }
            return;
        }
        TileGate tileGate = getGateTile(state, te);
        if (tileGate == null) return;
        Map<Side, Byte> map = getSidePower(state, tileGate);
        boolean powerChanged = tileGate.updateStates(map, true);
        if (powerChanged) {
            Block blockToTick = te instanceof TileBPMultipart bpMultipart ? bpMultipart.getBlockState().getBlock() : this;
            if (!level.getBlockTicks().willTickThisTick(pos, blockToTick)) {
                level.scheduleTick(pos, blockToTick,
                        getDelay(state, tileGate), TickPriority.HIGH);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        TileGate tileGate = getGateTile(state, level.getBlockEntity(pos));
        if (tileGate == null) return;
        Map<Side, Byte> map = getSidePower(state, tileGate);
        boolean powerChanged = tileGate.updateStates(map, false);
        if (powerChanged) {
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            for (Side side : Side.values()){
                Direction dir = toDirection(side, state);
                if (isSideSource(side, state, tileGate)){
                    BlockPos neighbor = pos.relative(dir);
                    BlockState neighborState = level.getBlockState(neighbor);
                    level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
                }
            }
        }
    }

    /**
     *  IBPartBlock
     */

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return AABBUtils.rotate(Refs.GATE_AABB, state.getValue(FACING));
    }
}
