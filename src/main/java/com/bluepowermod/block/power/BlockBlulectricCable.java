/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricCable extends BlockContainerBase{

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    public static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    protected final VoxelShape[] shapes = makeShapes();

    public BlockBlulectricCable() {
        super(Material.IRON, TileBlulectricCable.class);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP)
                .with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false));
        setRegistryName(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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
        return this.shapes[this.getShapeIndex(state)];
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
        if (!world.isRemote){
           world.setBlockState(pos, this.getStateForPos(world, pos), 2);
        }
        if (!world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolid()) {
            world.destroyBlock(pos, true);
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
            TileEntity tile = world.getTileEntity(pos.offset(face));
            if(tile != null) {
                switch (face) {
                    case NORTH:
                        connected_front = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent();
                        break;
                    case SOUTH:
                        connected_back = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent();
                        break;
                    case WEST:
                        connected_left = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent();
                        break;
                    case EAST:
                        connected_right = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent();
                        break;
                }
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
