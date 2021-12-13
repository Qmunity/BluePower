/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.gates;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author MoreThanHidden
 */
public class BlockGateBase extends BlockBase implements SimpleWaterloggedBlock {

    private final String name;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final BooleanProperty POWERED_FRONT = BooleanProperty.create("powered_front");
    public static final BooleanProperty POWERED_BACK = BooleanProperty.create("powered_back");
    public static final BooleanProperty POWERED_LEFT = BooleanProperty.create("powered_left");
    public static final BooleanProperty POWERED_RIGHT = BooleanProperty.create("powered_right");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockGateBase(String name) {
        super(Material.CLAY);
        this.name = name;
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

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return state.getValue(FACING) != side && (state.getValue(FACING).getOpposite() != side);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        Direction face = context.getClickedFace();
        return this.defaultBlockState().setValue(ROTATION, context.getHorizontalDirection().getOpposite().get2DDataValue()).setValue(FACING, face).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
        Map<String, Byte> map = getSidePower(world, state, pos);
        world.setBlockAndUpdate(pos, state.setValue(POWERED_FRONT, map.get("front") > 0)
                .setValue(POWERED_BACK, map.get("back") > 0)
                .setValue(POWERED_LEFT, map.get("left") > 0)
                .setValue(POWERED_RIGHT, map.get("right") > 0));
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side){
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        if(side == dirs[blockState.getValue(ROTATION)]) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));
        if(side == dirs[blockState.getValue(ROTATION)]) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    private Map<String, Byte> getSidePower(BlockGetter worldIn, BlockState state, BlockPos pos){
         Map<String, Byte> map = new HashMap<>();
         Direction[] dirs = DirectionHelper.ArrayFromDirection(state.getValue(FACING));
         Direction side_left = dirs[state.getValue(ROTATION) == 3 ? 0 : state.getValue(ROTATION) + 1];
         Direction side_right = side_left.getOpposite();
         Direction side_back = dirs[state.getValue(ROTATION)];
         BlockPos pos_left = pos.relative(side_left);
         BlockPos pos_right = pos.relative(side_right);
         BlockPos pos_back = pos.relative(side_back);
         BlockState state_left = worldIn.getBlockState(pos_left);
         BlockState state_right = worldIn.getBlockState(pos_right);
         BlockState state_back = worldIn.getBlockState(pos_back);
         byte left = (byte) state_left.getSignal(worldIn, pos_left, side_right);
         byte right = (byte) state_right.getSignal(worldIn, pos_right, side_left);
         byte back = (byte) state_back.getSignal(worldIn, pos_back, side_back.getOpposite());
         if(state_left.getBlock() instanceof RedStoneWireBlock){left = state_left.getValue(RedStoneWireBlock.POWER).byteValue();}
         if(state_right.getBlock() instanceof RedStoneWireBlock){right = state_right.getValue(RedStoneWireBlock.POWER).byteValue();}
         if(state_back.getBlock() instanceof RedStoneWireBlock){back = state_back.getValue(RedStoneWireBlock.POWER).byteValue();}
         map.put("left", left);
         map.put("right", right);
         map.put("back", back);
         map.put("front", computeRedstone(back, left, right));
         return map;
    }

    public byte computeRedstone(byte back, byte left, byte right){

        if (left > 0 && right > 0 ){
            return (byte)(back > 0 ? 16 : 0);
        }
        return 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        if(!world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isCollisionShapeFullBlock(world,pos.relative(state.getValue(FACING).getOpposite()))) {
            world.destroyBlock(pos, true);
            return;
        }
        Map<String, Byte> map = getSidePower(world, state, pos);
        world.setBlockAndUpdate(pos, state.setValue(POWERED_FRONT, map.get("front") > 0)
                .setValue(POWERED_BACK, map.get("back") > 0)
                .setValue(POWERED_LEFT, map.get("left") > 0)
                .setValue(POWERED_RIGHT, map.get("right") > 0));
    }

}
