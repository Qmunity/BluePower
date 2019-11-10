package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

public class BlockLampSurface extends BlockLamp implements IWaterLoggable {

    private final String name;
    private final Boolean isInverted;
    private final VoxelShape size;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockLampSurface(String name, boolean isInverted, MinecraftColor color, VoxelShape size) {
        super(name, isInverted, color);
        this.name = name;
        this.isInverted = isInverted;
        this.size = size;
        this.setDefaultState(stateContainer.getBaseState().with(POWER, isInverted ? 15 : 0).with(FACING, Direction.UP).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABBUtils.rotate(size, state.get(FACING));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initModel() {

    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWER, FACING, WATERLOGGED);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        if (!world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid()) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand).with(FACING, facing).with(WATERLOGGED, false);
    }

}
