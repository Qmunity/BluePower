package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
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

import javax.annotation.Nullable;

public class BlockLampSurface extends BlockLamp {

    private final String name;
    private final Boolean isInverted;
    private final VoxelShape size;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockLampSurface(String name, boolean isInverted, MinecraftColor color, VoxelShape size) {
        super(name, isInverted, color);
        this.name = name;
        this.isInverted = isInverted;
        this.size = size;
        this.setDefaultState(stateContainer.getBaseState().with(POWER, isInverted ? 15 : 0).with(FACING, Direction.UP));
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
        builder.add(POWER, FACING);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        if (!world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid()) {
            world.destroyBlock(pos, true);
        }
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
        return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand).with(FACING, facing);
    }

}
