/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block;

import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

/**
 * @author MoreThanHidden
 */
public class BlockContainerHorizontalFacingBase extends BlockContainerBase {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockContainerHorizontalFacingBase(Class<? extends TileBase> tileEntityClass) {
        super(tileEntityClass);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, ACTIVE);
    }

    public static void setState(boolean active, Level worldIn, BlockPos pos){
        BlockState iblockstate = worldIn.getBlockState(pos);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        worldIn.setBlock(pos, iblockstate.setValue(ACTIVE, active), 3);
        if (tileentity != null){
            tileentity.clearRemoved();
            worldIn.setBlockEntity(tileentity);
        }
    }
    public static void setState(Direction facing, Level worldIn, BlockPos pos){
        BlockState iblockstate = worldIn.getBlockState(pos);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        worldIn.setBlock(pos, iblockstate.setValue(FACING, facing), 3);
        if (tileentity != null){
            tileentity.clearRemoved();
            worldIn.setBlockEntity(tileentity);
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack iStack) {
        super.setPlacedBy(world, pos, state, placer, iStack);
        world.setBlock(pos, state.setValue(FACING, canRotateVertical() ? Direction.orderedByNearest(placer)[0] : placer.getDirection().getOpposite()), 2);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        if(direction == Rotation.CLOCKWISE_90) {
            switch (state.getValue(FACING)) {
                case NORTH:
                    state = state.setValue(FACING, Direction.EAST);
                    break;
                case EAST:
                    state = state.setValue(FACING, Direction.SOUTH);
                    break;
                case SOUTH:
                    state = state.setValue(FACING, Direction.WEST);
                    break;
                case WEST:
                    state = state.setValue(FACING, Direction.NORTH);
                    break;
            }
        }else if(direction == Rotation.COUNTERCLOCKWISE_90){
            switch (state.getValue(FACING)) {
                case NORTH:
                    state = state.setValue(FACING, Direction.WEST);
                    break;
                case WEST:
                    state = state.setValue(FACING, Direction.SOUTH);
                    break;
                case SOUTH:
                    state = state.setValue(FACING, Direction.EAST);
                    break;
                case EAST:
                    state = state.setValue(FACING, Direction.NORTH);
                    break;
            }
        }else if (direction == Rotation.CLOCKWISE_180){
            state = state.setValue(FACING, state.getValue(FACING).getOpposite());
        }
        world.setBlock(pos, state, 2);
        return state;
    }
}
