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
import com.bluepowermod.util.AABBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.Rotation;
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

/**
 * @author MoreThanHidden
 */
public abstract class BlockGateBase extends BlockBase implements SimpleWaterloggedBlock, IBPPartBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final BooleanProperty POWERED_FRONT = BooleanProperty.create("powered_front");
    public static final BooleanProperty POWERED_BACK = BooleanProperty.create("powered_back");
    public static final BooleanProperty POWERED_LEFT = BooleanProperty.create("powered_left");
    public static final BooleanProperty POWERED_RIGHT = BooleanProperty.create("powered_right");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public enum Side {
        BACK,
        FRONT,
        LEFT,
        RIGHT
    }

    public BlockGateBase() {
        super();
        this.registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(POWERED_BACK, false)
                .setValue(POWERED_FRONT, false)
                .setValue(POWERED_LEFT, false)
                .setValue(POWERED_RIGHT, false)
                .setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, ROTATION, POWERED_BACK, POWERED_FRONT, POWERED_LEFT, POWERED_RIGHT, WATERLOGGED);
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
        switch (rotation){
            case NONE -> {}
            case CLOCKWISE_90 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION),  1));
            case CLOCKWISE_180 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION), 2));
            case COUNTERCLOCKWISE_90 -> state = state.setValue(ROTATION, rotate(state.getValue(ROTATION), 3));
        }
        level.setBlock(pos, state, 3);
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
        Map<Side, Byte> map = getSidePower(world, state, pos);
        BlockState newState = state.setValue(POWERED_FRONT, map.get(Side.FRONT) > 0)
                .setValue(POWERED_BACK, map.get(Side.BACK) > 0)
                .setValue(POWERED_LEFT, map.get(Side.LEFT) > 0)
                .setValue(POWERED_RIGHT, map.get(Side.RIGHT) > 0);
        if (!(te instanceof TileBPMultipart tileBPMultipart)){
            //Change the block state
            world.setBlock(pos, newState, 2);
        }else{
            //Update the state in the Multipart
            tileBPMultipart.changeState(state, newState);
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side){
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        Side side1 = fromDirection(side.getOpposite(), blockState.getValue(ROTATION), dirs);
        if (isSideSource(side1, blockState, blockAccess, pos)){
            return redstoneFromSide(side1, blockState);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        Side side1 = fromDirection(side.getOpposite(), blockState.getValue(ROTATION), dirs);
        if (isSideSource(side1, blockState, blockAccess, pos)){
            return redstoneFromSide(side1, blockState);
        }
        return 0;
    }

    private int redstoneFromSide(Side side, BlockState state){
        return switch (side){
            case FRONT -> state.getValue(POWERED_FRONT) ? 16 : 0;
            case BACK -> state.getValue(POWERED_BACK) ? 16 : 0;
            case LEFT -> state.getValue(POWERED_LEFT) ? 16 : 0;
            case RIGHT -> state.getValue(POWERED_RIGHT) ? 16 : 0;
        };
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

    protected abstract Map<Side, Byte> getSidePower(SignalGetter worldIn, BlockState state, BlockPos pos);

    protected boolean isSideSource(Side side, BlockState blockState, BlockGetter blockAccess, BlockPos pos){
        return false;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }



    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        BlockEntity te = world.getBlockEntity(pos);
        if(!world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isCollisionShapeFullBlock(world,pos.relative(state.getValue(FACING).getOpposite()))) {
            if (te instanceof TileBPMultipart tileBPMultipart) {
                tileBPMultipart.removeState(state);
            } else {
                world.destroyBlock(pos, true);
            }
            return;
        }
        Map<Side, Byte> map = getSidePower(world, state, pos);
        BlockState newState = state.setValue(POWERED_FRONT, map.get(Side.FRONT) > 0)
                .setValue(POWERED_BACK, map.get(Side.BACK) > 0)
                .setValue(POWERED_LEFT, map.get(Side.LEFT) > 0)
                .setValue(POWERED_RIGHT, map.get(Side.RIGHT) > 0);
        if (newState != state) {
            if (te instanceof TileBPMultipart tileBPMultipart) {
                tileBPMultipart.changeState(state, newState);
            } else {
                world.setBlockAndUpdate(pos, newState);
            }
            for (Direction dir : DirectionHelper.ArrayFromDirection(state.getValue(FACING))){
               BlockPos neighbor = pos.relative(dir);
                BlockState neighborState = world.getBlockState(neighbor);
               if (neighbor.equals(fromPos)) continue;
               world.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
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
