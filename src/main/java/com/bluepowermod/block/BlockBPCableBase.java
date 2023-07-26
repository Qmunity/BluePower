package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;

public class BlockBPCableBase extends BlockBase implements IBPPartBlock, SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    protected static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    protected static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    protected static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty JOIN_FRONT = BooleanProperty.create("join_front");
    public static final BooleanProperty JOIN_BACK = BooleanProperty.create("join_back");
    public static final BooleanProperty JOIN_LEFT = BooleanProperty.create("join_left");
    public static final BooleanProperty JOIN_RIGHT = BooleanProperty.create("join_right");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final VoxelShape[] shapes;


    public BlockBPCableBase(float width, float height) {
        shapes = makeShapes(width, height);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP)
                .setValue(CONNECTED_FRONT, false).setValue(CONNECTED_BACK, false)
                .setValue(CONNECTED_LEFT, false).setValue(CONNECTED_RIGHT, false)
                .setValue(JOIN_FRONT, false).setValue(JOIN_BACK, false)
                .setValue(JOIN_LEFT, false).setValue(JOIN_RIGHT, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        this.onMultipartReplaced(state, worldIn, pos, newState, isMoving);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void onMultipartReplaced(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        FACING.getPossibleValues().forEach(f ->{
            BlockPos neighborPos = pos.relative(f).relative(state.getValue(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, isMoving);
        });
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
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(worldIn, pos, state, livingEntity, itemStack);
        FACING.getPossibleValues().forEach(f -> {
            BlockPos neighborPos = pos.relative(f).relative(state.getValue(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, false);
        });
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).canOcclude();
    }

    protected Capability<?> getCapability(){
        return null;
    }

    private VoxelShape[] makeShapes(float width, float height) {
        float gap = 0;

        float f = 8.0F - width;
        float f1 = 8.0F + width;
        float f2 = 8.0F - width;
        float f3 = 8.0F + width;

        VoxelShape voxelshape = Block.box((double)f, 0, (double)f, (double)f1, (double)height, (double)f1);
        VoxelShape voxelshape1 = Block.box((double)f2, (double)gap, 0, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape2 = Block.box((double)f2, (double)gap, (double)f2, (double)f3, (double)height, 16.0D);
        VoxelShape voxelshape3 = Block.box(0, (double)gap, (double)f2, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape4 = Block.box((double)f2, (double)gap, (double)f2, 16.0D, (double)height, (double)f3);
        VoxelShape voxelshape5 = Shapes.or(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = Shapes.or(voxelshape2, voxelshape3);

        VoxelShape[] avoxelshape = new VoxelShape[]{
                Shapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1,
                Shapes.or(voxelshape2, voxelshape1), Shapes.or(voxelshape3, voxelshape1),
                Shapes.or(voxelshape6, voxelshape1), voxelshape4, Shapes.or(voxelshape2, voxelshape4),
                Shapes.or(voxelshape3, voxelshape4), Shapes.or(voxelshape6, voxelshape4), voxelshape5,
                Shapes.or(voxelshape2, voxelshape5), Shapes.or(voxelshape3, voxelshape5),
                Shapes.or(voxelshape6, voxelshape5),
                Block.box(f2,0,-height,f3, height,0),
                Block.box(f2,0,16 - height,f3, height,16),
                Block.box(-height,0,f2,0, height,f3),
                Block.box(16 - height,0,f2,16, height,f3)
        };

        for(int i = 0; i < 16; ++i) {
            avoxelshape[i] = Shapes.or(voxelshape, avoxelshape[i]);
        }

        return avoxelshape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape shapes = this.shapes[this.getShapeIndex(state)];

        //Draw the joins
        if(state.getValue(JOIN_FRONT))
            shapes = Shapes.or(shapes, this.shapes[16]);
        //if(state.getValue(JOIN_BACK))
            //shapes = Shapes.or(shapes, this.shapes[17]);
        if(state.getValue(JOIN_LEFT))
            shapes = Shapes.or(shapes, this.shapes[18]);
        //if(state.getValue(JOIN_RIGHT))
            //shapes = Shapes.or(shapes, this.shapes[19]);

        return AABBUtils.rotate(shapes, state.getValue(FACING));
    }

    private int getShapeIndex(BlockState state) {
        int i = 0;

        if(state.getValue(CONNECTED_FRONT))
            i |= getMask(Direction.NORTH);
        if(state.getValue(CONNECTED_BACK))
            i |= getMask(Direction.SOUTH);
        if(state.getValue(CONNECTED_LEFT))
            i |= getMask(Direction.WEST);
        if(state.getValue(CONNECTED_RIGHT))
            i |= getMask(Direction.EAST);

        return i;
    }

    private static int getMask(Direction facing) {
        return 1 << facing.get2DDataValue();
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        BlockEntity te = world.getBlockEntity(pos);
        //Get new state based on surrounding capabilities
        BlockState newState = getStateForPos(world, pos, defaultBlockState().setValue(FACING, state.getValue(FACING)), state.getValue(FACING));

        if (!(te instanceof TileBPMultipart)){
            //Change the block state
            world.setBlock(pos, newState, 2);
        }else{
            //Update the state in the Multipart
            ((TileBPMultipart) te).changeState(state, newState);
        }
        state = newState;

        //If not placed on a solid block break off
        if (!world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).canOcclude()) {
            if(te instanceof TileBPMultipart){
                ((TileBPMultipart)te).removeState(state);
            }else {
                world.destroyBlock(pos, true);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT, WATERLOGGED);
    }

    //Returns true if a given blockState / tileEntity can connect.
    protected boolean canConnect(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity tileEntity, Direction direction){
        if (tileEntity != null) {
            return tileEntity.getCapability(getCapability(), direction).isPresent();
        }else{
            return false;
        }
    }

    private BlockState getStateForPos(Level world, BlockPos pos, BlockState state, Direction face){
        List<Direction> directions = new ArrayList<>(FACING.getPossibleValues());
        List<Direction> internal = null;
        boolean connected_left = false;
        boolean connected_right = false;
        boolean connected_front = false;
        boolean connected_back = false;
        boolean join_left = false;
        boolean join_right = false;
        boolean join_front = false;
        boolean join_back = false;

        //Make sure the side we are trying to connect on isn't blocked.
        BlockEntity ownTile = world.getBlockEntity(pos);
        if(ownTile instanceof TileBPMultipart) {
            directions.removeIf(d -> ((TileBPMultipart) ownTile).isSideBlocked(getCapability(), d));
            internal = ((TileBPMultipart) ownTile).getStates().stream().filter(s -> s.getBlock() == this).map(s -> s.getValue(FACING)).collect(Collectors.toList());
        }
        //Make sure the cable is on the same side of the block
        directions.removeIf(d -> {
            BlockEntity t = world.getBlockEntity(pos.relative(d));
            return (world.getBlockState(pos.relative(d)).getBlock() == this
                    && world.getBlockState(pos.relative(d)).getValue(FACING) != face)
                    || (t instanceof TileBPMultipart
                    && ((TileBPMultipart) t).getStates().stream().noneMatch(s -> s.getValue(FACING) == face));
        });

        //Populate all directions
        for (Direction d : directions) {
            BlockEntity tileEntity = world.getBlockEntity(pos.relative(d));
            BlockState dirState = world.getBlockState(pos.relative(d));
            BlockPos dirPos = pos.relative(d);

            boolean join = false;
            //If Air look for a change in Direction
            if (world.getBlockState(pos.relative(d)).getBlock() == Blocks.AIR) {
                dirState = world.getBlockState(pos.relative(d).relative(face.getOpposite()));
                dirPos = pos.relative(d).relative(face.getOpposite());
                if (dirState.getBlock() == this && dirState.getValue(FACING) == d) {
                    tileEntity = world.getBlockEntity(pos.relative(d).relative(face.getOpposite()));
                    join = true;
                } else if (dirState.getBlock() instanceof BlockBPMultipart) {
                    tileEntity = world.getBlockEntity(pos.relative(d).relative(face.getOpposite()));
                    if (tileEntity instanceof TileBPMultipart && ((TileBPMultipart) tileEntity).getStates().stream().filter(s -> s.getBlock() == this).anyMatch(s -> s.getValue(FACING) == d)) {
                        join = true;
                    } else {
                        tileEntity = null;
                    }
                }
            }

            //Check Capability for Direction
                switch (state.getValue(FACING)) {
                    case UP:
                    case DOWN:
                        switch (d) {
                            case EAST:
                                connected_right = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_right = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case WEST:
                                connected_left = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_left = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case NORTH:
                                connected_front = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_front = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case SOUTH:
                                connected_back = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_back = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                            case WEST:
                                connected_right = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_right = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case EAST:
                                connected_left = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_left = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case UP:
                                connected_front = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_front = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case DOWN:
                                connected_back = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_back = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                            case EAST:
                                connected_right = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_right = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case WEST:
                                connected_left = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_left = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case UP:
                                connected_front = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_front = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case DOWN:
                                connected_back = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_back = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                            case NORTH:
                                connected_right = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_right = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case SOUTH:
                                connected_left = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_left = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case UP:
                                connected_front = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_front = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case DOWN:
                                connected_back = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_back = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                            case SOUTH:
                                connected_right = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_right = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case NORTH:
                                connected_left = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_left = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case UP:
                                connected_front = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_front = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                            case DOWN:
                                connected_back = canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                join_back = join && canConnect(world, dirPos, dirState, tileEntity, d.getOpposite());
                                break;
                        }
                }
            }

        if (internal != null)
            for(Direction d : internal){
                switch (state.getValue(FACING)) {
                    case UP:
                    case DOWN:
                        switch (d) {
                            case EAST:
                                connected_left = true;
                                break;
                            case WEST:
                                connected_right = true;
                                break;
                            case NORTH:
                                connected_back = true;
                                break;
                            case SOUTH:
                                connected_front = true;
                                break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                            case WEST:
                                connected_left = true;
                                break;
                            case EAST:
                                connected_right = true;
                                break;
                            case UP:
                                connected_back = true;
                                break;
                            case DOWN:
                                connected_front = true;
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                            case EAST:
                                connected_left = true;
                                break;
                            case WEST:
                                connected_right = true;
                                break;
                            case UP:
                                connected_back = true;
                                break;
                            case DOWN:
                                connected_front = true;
                                break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                            case NORTH:
                                connected_left = true;
                                break;
                            case SOUTH:
                                connected_right = true;
                                break;
                            case UP:
                                connected_back = true;
                                break;
                            case DOWN:
                                connected_front = true;
                                break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                            case SOUTH:
                                connected_left = true;
                                break;
                            case NORTH:
                                connected_right = true;
                                break;
                            case UP:
                                connected_back = true;
                                break;
                            case DOWN:
                                connected_front = true;
                                break;
                        }
                }
            }

        FluidState fluidstate = world.getFluidState(pos);
        return state.setValue(CONNECTED_LEFT, connected_left)
                .setValue(CONNECTED_RIGHT, connected_right)
                .setValue(CONNECTED_FRONT, connected_front)
                .setValue(CONNECTED_BACK, connected_back)
                .setValue(JOIN_LEFT, join_left)
                .setValue(JOIN_RIGHT, join_right)
                .setValue(JOIN_FRONT, join_front)
                .setValue(JOIN_BACK, join_back).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStateForPos(context.getLevel(), context.getClickedPos(), defaultBlockState().setValue(FACING, context.getClickedFace()), context.getClickedFace());
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return AABBUtils.rotate(this.shapes[this.getShapeIndex(state)], state.getValue(FACING));
    }
}