/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier3.TileBlulectricCable;

import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricCable extends BlockContainerBase implements IBPPartBlock, IWaterLoggable {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    private static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    private static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    private static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    private static final BooleanProperty JOIN_FRONT = BooleanProperty.create("join_front");
    private static final BooleanProperty JOIN_BACK = BooleanProperty.create("join_back");
    private static final BooleanProperty JOIN_LEFT = BooleanProperty.create("join_left");
    private static final BooleanProperty JOIN_RIGHT = BooleanProperty.create("join_right");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final VoxelShape[] shapes = makeShapes();

    public BlockBlulectricCable() {
        super(Material.IRON, TileBlulectricCable.class);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP)
                .with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false)
                .with(JOIN_FRONT, false).with(JOIN_BACK, false)
                .with(JOIN_LEFT, false).with(JOIN_RIGHT, false)
                .with(WATERLOGGED, false));
        setRegistryName(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        FACING.getAllowedValues().forEach(f ->{
                BlockPos neighborPos = pos.offset(f).offset(state.get(FACING).getOpposite());
                worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, isMoving);
        });
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        super.onBlockPlacedBy(worldIn, pos, state, p_180633_4_, p_180633_5_);
        FACING.getAllowedValues().forEach(f -> {
            BlockPos neighborPos = pos.offset(f).offset(state.get(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, false);
        });
    }


    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid();
    }

    protected VoxelShape[] makeShapes() {

        float width = 2;
        float gap = 0;
        float height = 2;

        float f = 8.0F - width;
        float f1 = 8.0F + width;
        float f2 = 8.0F - width;
        float f3 = 8.0F + width;

        VoxelShape voxelshape = Block.makeCuboidShape((double)f, 0.0D, (double)f, (double)f1, (double)height, (double)f1);
        VoxelShape voxelshape1 = Block.makeCuboidShape((double)f2, (double)gap, 0.0D, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape2 = Block.makeCuboidShape((double)f2, (double)gap, (double)f2, (double)f3, (double)height, 16.0D);
        VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, (double)gap, (double)f2, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape4 = Block.makeCuboidShape((double)f2, (double)gap, (double)f2, 16.0D, (double)height, (double)f3);
        VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);

        VoxelShape[] avoxelshape = new VoxelShape[]{
                VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1,
                VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1),
                VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4),
                VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5,
                VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5),
                VoxelShapes.or(voxelshape6, voxelshape5)
        };

        for(int i = 0; i < 16; ++i) {
            avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
        }

        return avoxelshape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABBUtils.rotate(this.shapes[this.getShapeIndex(state)], state.get(FACING));
    }

    private int getShapeIndex(BlockState state) {
        int i = 0;

        if(state.get(CONNECTED_FRONT))
            i |= getMask(Direction.NORTH);
        if(state.get(CONNECTED_BACK))
            i |= getMask(Direction.SOUTH);
        if(state.get(CONNECTED_LEFT))
            i |= getMask(Direction.WEST);
        if(state.get(CONNECTED_RIGHT))
            i |= getMask(Direction.EAST);

        return i;
    }

    private static int getMask(Direction facing) {
        return 1 << facing.getHorizontalIndex();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        TileEntity te = world.getTileEntity(pos);
        //Get new state based on surrounding capabilities
        BlockState newState = getStateForPos(world, pos, getDefaultState().with(FACING, state.get(FACING)), state.get(FACING));

        if (!(te instanceof TileBPMultipart)){
           //Change the block state
           world.setBlockState(pos, newState, 2);
        }else{
            //Update the state in the Multipart
            ((TileBPMultipart) te).changeState(state, newState);
        }
        state = newState;
        //If not placed on a solid block break off
        if (!world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid()) {
            if(te instanceof TileBPMultipart){
                ((TileBPMultipart)te).removeState(state);
                if(world instanceof ServerWorld) {
                    NonNullList<ItemStack> drops = NonNullList.create();
                    drops.add(new ItemStack(this));
                    InventoryHelper.dropItems(world, pos, drops);
                }
            }else {
                world.destroyBlock(pos, true);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT, WATERLOGGED);
    }

    private BlockState getStateForPos(World world, BlockPos pos, BlockState state, Direction face){
        List<Direction> directions = new ArrayList<>(FACING.getAllowedValues());
        //Make sure the side we are trying to connect on isn't blocked.
        TileEntity ownTile = world.getTileEntity(pos);
        if(ownTile instanceof TileBPMultipart)
            directions.removeIf(d -> ((TileBPMultipart) ownTile).isSideBlocked(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d));
        //Make sure the cable is on the same side of the block
        directions.removeIf(d -> world.getBlockState(pos.offset(d)).getBlock() instanceof BlockBlulectricCable
                && world.getBlockState(pos.offset(d)).get(FACING) != face);

        //Populate all directions
        for (Direction d : directions) {
            TileEntity tileEntity = world.getTileEntity(pos.offset(d));

            boolean join = false;
            //If Air or Water look for a change in Direction
            Block dBlock = world.getBlockState(pos.offset(d)).getBlock();
            if ((dBlock == Blocks.AIR || dBlock == Blocks.WATER) &&
                    world.getBlockState(pos.offset(d).offset(face.getOpposite())).getBlock() instanceof BlockBlulectricCable &&
                    world.getBlockState(pos.offset(d).offset(face.getOpposite())).get(FACING) == d) {
                tileEntity = world.getTileEntity(pos.offset(d).offset(face.getOpposite()));
                join = true;
            }

            //Check Capability for Direction
            if (tileEntity != null)
                switch (state.get(FACING)) {
                    case UP:
                    case DOWN:
                        switch (d) {
                            case EAST:
                                state = state.with(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case WEST:
                                state = state.with(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case NORTH:
                                state = state.with(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case SOUTH:
                                state = state.with(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                            case WEST:
                                state = state.with(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case EAST:
                                state = state.with(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.with(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.with(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                            case EAST:
                                state = state.with(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case WEST:
                                state = state.with(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.with(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.with(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                            case NORTH:
                                state = state.with(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case SOUTH:
                                state = state.with(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.with(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.with(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                            case SOUTH:
                                state = state.with(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case NORTH:
                                state = state.with(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.with(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.with(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.with(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                }
        }
        IFluidState fluidstate = world.getFluidState(pos);
        return state.with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getStateForPos(context.getWorld(), context.getPos(), getDefaultState().with(FACING, context.getFace()), context.getFace());
    }

}
