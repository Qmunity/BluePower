package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBlulectricCable;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class BlockBlulectricCable extends BlockContainerBase{

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    public static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");

    public BlockBlulectricCable() {
        super(Material.ROCK, TileBlulectricCable.class);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false).with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false));
        setRegistryName(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        if (!world.isRemote){
           world.setBlockState(pos, this.getStateForPos(world, pos), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT);
    }

    private BlockState getStateForPos(World world, BlockPos pos){
        boolean connected_back = false;
        boolean connected_front = false;
        boolean connected_left = false;
        boolean connected_right = false;

        for (Direction face : FACING.getAllowedValues()){
            BlockState stateof = world.getBlockState(pos.offset(face));
            switch (face){
                case NORTH:
                    connected_front = stateof.canProvidePower();
                    break;
                case SOUTH:
                    connected_back = stateof.canProvidePower();
                    break;
                case WEST:
                    connected_left  = stateof.canProvidePower();
                    break;
                case EAST:
                    connected_right  = stateof.canProvidePower();
                    break;
            }
        }

        return this.getDefaultState().with(CONNECTED_RIGHT, connected_right)
                .with(CONNECTED_LEFT, connected_left)
                .with(CONNECTED_FRONT, connected_front)
                .with(CONNECTED_BACK, connected_back);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
       return getStateForPos(context.getWorld(), context.getPos());
    }

}
