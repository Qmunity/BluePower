package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Locale;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;

import static net.minecraft.core.Direction.*;

public class BlockBPCableBase extends BlockBase implements IBPPartBlock, SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final EnumProperty<ConnectionType> CONNECTION_TYPE_FRONT = EnumProperty.create("connection_type_front", ConnectionType.class);
    protected static final EnumProperty<ConnectionType> CONNECTION_TYPE_BACK = EnumProperty.create("connection_type_back", ConnectionType.class);
    protected static final EnumProperty<ConnectionType> CONNECTION_TYPE_LEFT = EnumProperty.create("connection_type_left", ConnectionType.class);
    protected static final EnumProperty<ConnectionType> CONNECTION_TYPE_RIGHT = EnumProperty.create("connection_type_right", ConnectionType.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final VoxelShape[] shapes;


    public BlockBPCableBase(float width, float height) {
        shapes = makeShapes(width, height);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP)
                .setValue(CONNECTION_TYPE_FRONT, ConnectionType.NONE).setValue(CONNECTION_TYPE_BACK, ConnectionType.NONE)
                .setValue(CONNECTION_TYPE_LEFT, ConnectionType.NONE).setValue(CONNECTION_TYPE_RIGHT, ConnectionType.NONE)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        this.onMultipartReplaced(state, worldIn, pos, newState, isMoving);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void onMultipartReplaced(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        FACING.getPossibleValues().forEach(f -> {
            BlockPos neighborPos = pos.relative(f).relative(state.getValue(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, false);
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
        if(state.getValue(CONNECTION_TYPE_FRONT) == ConnectionType.OUTER_CORNER)
            shapes = Shapes.or(shapes, this.shapes[16]);
        //if(state.getValue(JOIN_BACK))
            //shapes = Shapes.or(shapes, this.shapes[17]);
        if(state.getValue(CONNECTION_TYPE_LEFT) == ConnectionType.OUTER_CORNER)
            shapes = Shapes.or(shapes, this.shapes[18]);
        //if(state.getValue(JOIN_RIGHT))
            //shapes = Shapes.or(shapes, this.shapes[19]);

        return AABBUtils.rotate(shapes, state.getValue(FACING));
    }

    private int getShapeIndex(BlockState state) {
        int i = 0;

        if(state.getValue(CONNECTION_TYPE_FRONT) != ConnectionType.NONE)
            i |= getMask(Direction.NORTH);
        if(state.getValue(CONNECTION_TYPE_BACK) != ConnectionType.NONE)
            i |= getMask(Direction.SOUTH);
        if(state.getValue(CONNECTION_TYPE_LEFT) != ConnectionType.NONE)
            i |= getMask(WEST);
        if(state.getValue(CONNECTION_TYPE_RIGHT) != ConnectionType.NONE)
            i |= getMask(Direction.EAST);

        return i;
    }

    private static int getMask(Direction facing) {
        return 1 << facing.get2DDataValue();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        updateState(state, level, pos, blockIn, fromPos, bool);
    }

    protected BlockState updateState(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean movedByPiston){
        BlockEntity te = level.getBlockEntity(pos);
        //Get new state based on surrounding capabilities
        BlockState newState = getStateForPos(level, pos, defaultBlockState().setValue(FACING, state.getValue(FACING)), state.getValue(FACING));

        if (!(te instanceof TileBPMultipart multipart)){
            //Change the block state
            level.setBlock(pos, newState, 2);
        }else{
            //Update the state in the Multipart
            multipart.changeState(state, newState);
        }
        state = newState;

        //If not placed on a solid block break off
        if (!level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).canOcclude()) {
            if(te instanceof TileBPMultipart multipart){
                multipart.removeState(state);
            }else {
                level.destroyBlock(pos, true);
            }
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTION_TYPE_FRONT, CONNECTION_TYPE_BACK, CONNECTION_TYPE_LEFT, CONNECTION_TYPE_RIGHT, WATERLOGGED);
    }

    //Returns true if a given blockState / tileEntity can connect.
    protected boolean canConnect(Level world, BlockPos neighborPos, BlockState neighborState, @Nullable BlockEntity neighborTileEntity, Direction direction){
        if (neighborTileEntity != null) {
            return neighborTileEntity.getCapability(getCapability(), direction).isPresent();
        }else{
            return false;
        }
    }

    protected boolean isNeighborStateEquivalent(BlockState state, BlockEntity be, BlockState neighborState, BlockEntity neighborBE){
        return neighborState.getBlock() == state.getBlock();
    }

    private BlockState getStateForPos(Level world, BlockPos pos, BlockState state, Direction face){
        Direction[] sides = directionsFromFacing(face);
        ConnectionType[] connections = new ConnectionType[]{ConnectionType.NONE, ConnectionType.NONE, ConnectionType.NONE, ConnectionType.NONE};

        BlockEntity ownTile = world.getBlockEntity(pos);
        TileBPMultipart multipart = null;
        if (ownTile instanceof TileBPMultipart bpMultipart){
            multipart = bpMultipart;
            ownTile = multipart.getTileForState(state);
        }
        outer:
        for (int i = 0; i < 4; i++){
            Direction side = sides[i];
            if (multipart != null) {
                if (multipart.isSideBlocked(getCapability(), side)) {
                    continue;
                }
                for (BlockState s : multipart.getStates()){
                    if (isNeighborStateEquivalent(state, ownTile, s, multipart.getTileForState(s))){
                        if (s.getValue(FACING) == side.getOpposite()){
                            connections[i] = s.getBlock() == this ? ConnectionType.INNER_CORNER : ConnectionType.STRAIGHT;
                            continue outer;
                        }
                    }
                }

            }
            BlockPos neighbor = pos.relative(side);
            BlockState neighborState = world.getBlockState(neighbor);
            BlockEntity neighborTile = world.getBlockEntity(neighbor);
            boolean checkAroundCorner = false;
            if (isNeighborStateEquivalent(state, ownTile, neighborState, neighborTile)){
                if (neighborState.getValue(FACING) == face) {
                    connections[i] = ConnectionType.STRAIGHT;
                    continue;
                }
                checkAroundCorner = true;
            } else if (neighborState.getBlock() == Blocks.AIR){
                checkAroundCorner = true;
            } else if (neighborTile instanceof TileBPMultipart neighborMultipart){
                if (neighborMultipart.isSideBlocked(getCapability(), side.getOpposite())) {
                    continue;
                }
                for (BlockState s : neighborMultipart.getStates()){
                    BlockEntity partBE = neighborMultipart.getTileForState(s);
                    if (isNeighborStateEquivalent(state, ownTile, s, partBE)) {
                        if (s.getValue(FACING) == face) {
                            connections[i] = ConnectionType.STRAIGHT;
                            continue outer;
                        } else {
                            checkAroundCorner = true;
                        }
                    }
                }
            }
            if (checkAroundCorner){
                neighbor = neighbor.relative(face.getOpposite());
                neighborState = world.getBlockState(neighbor);
                neighborTile = world.getBlockEntity(neighbor);
                if (neighborTile instanceof TileBPMultipart multipart1){
                    for (BlockState s : multipart1.getStates()){
                        if (isNeighborStateEquivalent(state, ownTile, s, multipart1.getTileForState(s)) && s.getValue(FACING) == side){
                            connections[i] = ConnectionType.OUTER_CORNER;
                            continue outer;
                        }
                    }
                }
                if (!isNeighborStateEquivalent(state, ownTile, neighborState, neighborTile) || neighborState.getValue(FACING) != side) continue;
                connections[i] = ConnectionType.OUTER_CORNER;
                continue;
            }
            if (canConnect(world, neighbor, neighborState, neighborTile, side)){
                connections[i] = ConnectionType.STRAIGHT;
            }
        }
        FluidState fluidstate = world.getFluidState(pos);
        return state.setValue(CONNECTION_TYPE_LEFT, connections[0])
                .setValue(CONNECTION_TYPE_RIGHT, connections[1])
                .setValue(CONNECTION_TYPE_FRONT, connections[2])
                .setValue(CONNECTION_TYPE_BACK, connections[3])
                .setValue(WATERLOGGED, fluidstate.is(Fluids.WATER));
    }

    private Direction[] directionsFromFacing(Direction facing){
        //Order is Left, Right, Front, Back
        return switch (facing){
            case UP, DOWN -> new Direction[]{WEST, EAST, NORTH, SOUTH};
            case NORTH -> new Direction[]{EAST, WEST, UP, DOWN};
            case SOUTH -> new Direction[]{WEST, EAST, UP, DOWN};
            case WEST -> new Direction[]{NORTH, SOUTH, UP, DOWN};
            case EAST -> new Direction[]{SOUTH, NORTH, UP, DOWN};
        };
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

    public enum ConnectionType implements StringRepresentable {
        NONE,
        STRAIGHT,
        INNER_CORNER,
        OUTER_CORNER;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}