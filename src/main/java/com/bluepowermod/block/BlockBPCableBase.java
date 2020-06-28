package com.bluepowermod.block;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockBPCableBase extends BlockBase implements IBPPartBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    protected static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    protected static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    protected static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    protected static final BooleanProperty JOIN_FRONT = BooleanProperty.create("join_front");
    protected static final BooleanProperty JOIN_BACK = BooleanProperty.create("join_back");
    protected static final BooleanProperty JOIN_LEFT = BooleanProperty.create("join_left");
    protected static final BooleanProperty JOIN_RIGHT = BooleanProperty.create("join_right");
    protected final VoxelShape[] shapes;


    public BlockBPCableBase(float width, float height) {
        super(Material.IRON);
        shapes = makeShapes(width, height);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP)
                .with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false)
                .with(JOIN_FRONT, false).with(JOIN_BACK, false)
                .with(JOIN_LEFT, false).with(JOIN_RIGHT, false));
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
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid();
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
                if(world instanceof ServerWorld) {
                    ((TileBPMultipart)te).removeState(state);
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
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT);
    }

    private BlockState getStateForPos(World world, BlockPos pos, BlockState state, Direction face){
        List<Direction> directions = new ArrayList<>(FACING.getAllowedValues());
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
        TileEntity ownTile = world.getTileEntity(pos);
        if(ownTile instanceof TileBPMultipart) {
            directions.removeIf(d -> ((TileBPMultipart) ownTile).isSideBlocked(getCapability(), d));
            internal = ((TileBPMultipart) ownTile).getStates().stream().filter(s -> s.getBlock() == this).map(s -> s.get(FACING)).collect(Collectors.toList());
        }
        //Make sure the cable is on the same side of the block
        directions.removeIf(d -> {
            TileEntity t = world.getTileEntity(pos.offset(d));
            return (world.getBlockState(pos.offset(d)).getBlock() == this
                    && world.getBlockState(pos.offset(d)).get(FACING) != face)
                    || (t instanceof TileBPMultipart
                    && ((TileBPMultipart) t).getStates().stream().noneMatch(s -> s.get(FACING) == face));
        });

        //Populate all directions
        for (Direction d : directions) {
            TileEntity tileEntity = world.getTileEntity(pos.offset(d));

            boolean join = false;
            //If Air look for a change in Direction
            if (world.getBlockState(pos.offset(d)).getBlock() == Blocks.AIR) {
                BlockState dirState = world.getBlockState(pos.offset(d).offset(face.getOpposite()));
                if (dirState.getBlock() == this && dirState.get(FACING) == d) {
                    tileEntity = world.getTileEntity(pos.offset(d).offset(face.getOpposite()));
                    join = true;
                } else if (dirState.getBlock() instanceof BlockBPMultipart) {
                    tileEntity = world.getTileEntity(pos.offset(d).offset(face.getOpposite()));
                    if (tileEntity instanceof TileBPMultipart && ((TileBPMultipart) tileEntity).getStates().stream().filter(s -> s.getBlock() == this).anyMatch(s -> s.get(FACING) == d)) {
                        join = true;
                    } else {
                        tileEntity = null;
                    }
                }
            }

            //Check Capability for Direction
            if (tileEntity != null) {
                switch (state.get(FACING)) {
                    case UP:
                    case DOWN:
                        switch (d) {
                            case EAST:
                                connected_right = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_right = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case WEST:
                                connected_left = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_left = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case NORTH:
                                connected_front = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_front = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case SOUTH:
                                connected_back = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_back = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                            case WEST:
                                connected_right = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_right = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case EAST:
                                connected_left = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_left = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case UP:
                                connected_front = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_front = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case DOWN:
                                connected_back = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_back = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                            case EAST:
                                connected_right = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_right = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case WEST:
                                connected_left = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_left = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case UP:
                                connected_front = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_front = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case DOWN:
                                connected_back = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_back = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                            case NORTH:
                                connected_right = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_right = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case SOUTH:
                                connected_left = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_left = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case UP:
                                connected_front = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_front = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case DOWN:
                                connected_back = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_back = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                            case SOUTH:
                                connected_right = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_right = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case NORTH:
                                connected_left = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_left = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case UP:
                                connected_front = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_front = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                            case DOWN:
                                connected_back = tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                join_back = join && tileEntity.getCapability(getCapability(), d.getOpposite()).isPresent();
                                break;
                        }
                }
            }
        }

        if (internal != null)
            for(Direction d : internal){
                switch (state.get(FACING)) {
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

        return state.with(CONNECTED_LEFT, connected_left)
                .with(CONNECTED_RIGHT, connected_right)
                .with(CONNECTED_FRONT, connected_front)
                .with(CONNECTED_BACK, connected_back)
                .with(JOIN_LEFT, join_left)
                .with(JOIN_RIGHT, join_right)
                .with(JOIN_FRONT, join_front)
                .with(JOIN_BACK, join_back);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getStateForPos(context.getWorld(), context.getPos(), getDefaultState().with(FACING, context.getFace()), context.getFace());
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return AABBUtils.rotate(this.shapes[this.getShapeIndex(state)], state.get(FACING));
    }
}