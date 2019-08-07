package com.bluepowermod.block.gates;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockGateBase extends BlockBase {

    private final String name;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final BooleanProperty POWERED_FRONT = BooleanProperty.create("powered_front");
    public static final BooleanProperty POWERED_BACK = BooleanProperty.create("powered_back");
    public static final BooleanProperty POWERED_LEFT = BooleanProperty.create("powered_left");
    public static final BooleanProperty POWERED_RIGHT = BooleanProperty.create("powered_right");

    public BlockGateBase(String name) {
        super(Material.CLAY);
        this.name = name;
        this.setDefaultState(stateContainer.getBaseState()
                .with(FACING, Direction.UP)
                .with(POWERED_BACK, false)
                .with(POWERED_FRONT, false)
                .with(POWERED_LEFT, false)
                .with(POWERED_RIGHT, false)
                .with(ROTATION, 0));
        setWIP(true);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, ROTATION, POWERED_BACK, POWERED_FRONT, POWERED_LEFT, POWERED_RIGHT);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Refs.GATE_AABB;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return state.get(FACING) != side && (state.get(FACING).getOpposite() != side);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ROTATION, context.getPlacementHorizontalFacing().getOpposite().getHorizontalIndex());
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {

        Map<String, Byte> map = getSidePower(world, state, pos1);

        return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand)
                .with(POWERED_FRONT, map.get("front") > 0)
                .with(POWERED_BACK, map.get("back") > 0)
                .with(POWERED_LEFT, map.get("left") > 0)
                .with(POWERED_RIGHT, map.get("right") > 0);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        Map<String, Byte> map = getSidePower(world, state, pos);
        world.setBlockState(pos, state.with(POWERED_FRONT, map.get("front") > 0)
                .with(POWERED_BACK, map.get("back") > 0)
                .with(POWERED_LEFT, map.get("left") > 0)
                .with(POWERED_RIGHT, map.get("right") > 0));
    }

    @Override
    public boolean canProvidePower(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side){
        if(side == Direction.byHorizontalIndex(blockState.get(ROTATION))) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if(side == Direction.byHorizontalIndex(blockState.get(ROTATION))) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    private Map<String, Byte> getSidePower(IBlockReader worldIn, BlockState state, BlockPos pos){
         Map<String, Byte> map = new HashMap<>();
         Direction side_left = Direction.byHorizontalIndex(state.get(ROTATION) == 3 ? 0 : state.get(ROTATION) + 1);
         Direction side_right = side_left.getOpposite();
         Direction side_back = Direction.byHorizontalIndex(state.get(ROTATION));
         BlockPos pos_left = pos.offset(side_left);
         BlockPos pos_right = pos.offset(side_right);
         BlockPos pos_back = pos.offset(side_back);
         BlockState state_left = worldIn.getBlockState(pos_left);
         BlockState state_right = worldIn.getBlockState(pos_right);
         BlockState state_back = worldIn.getBlockState(pos_back);
         byte left = (byte) state_left.getWeakPower(worldIn, pos_left, side_right);
         byte right = (byte) state_right.getWeakPower(worldIn, pos_right, side_left);
         byte back = (byte) state_back.getWeakPower(worldIn, pos_back, side_back.getOpposite());
         if(state_left.getBlock() instanceof RedstoneWireBlock){left = state_left.get(RedstoneWireBlock.POWER).byteValue();}
         if(state_right.getBlock() instanceof RedstoneWireBlock){right = state_right.get(RedstoneWireBlock.POWER).byteValue();}
         if(state_back.getBlock() instanceof RedstoneWireBlock){back = state_back.get(RedstoneWireBlock.POWER).byteValue();}
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
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        if(!world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            //TODO: Drop Block as Item
            //this.dropBlockAsItem(world, pos, state, 0);
        }
        Map<String, Byte> map = getSidePower(world, state, pos);
        world.setBlockState(pos, state.with(POWERED_FRONT, map.get("front") > 0)
                .with(POWERED_BACK, map.get("back") > 0)
                .with(POWERED_LEFT, map.get("left") > 0)
                .with(POWERED_RIGHT, map.get("right") > 0));
    }

}
