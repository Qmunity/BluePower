package com.bluepowermod.block.lighting;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.BlockGetter;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLampSurface extends BlockLamp implements SimpleWaterloggedBlock {

    private final VoxelShape size;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockLampSurface(String name, boolean isInverted, MinecraftColor color, VoxelShape size) {
        super(name, isInverted, color);
        this.size = size;
        this.registerDefaultState(stateDefinition.any().setValue(POWER, isInverted ? 15 : 0).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABBUtils.rotate(size, state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, FACING, WATERLOGGED);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        if (!world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).canOcclude()) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, Level worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).canOcclude();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(FACING, context.getClickedFace());
    }

}
