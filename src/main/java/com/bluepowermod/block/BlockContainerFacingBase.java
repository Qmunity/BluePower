/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block;

import com.bluepowermod.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MineMaarten
 */
public class BlockContainerFacingBase extends BlockContainerBase {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockContainerFacingBase(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material, tileEntityClass);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, ACTIVE);
    }

    public static void setState(boolean active, World worldIn, BlockPos pos){
        BlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, iblockstate.with(ACTIVE, active), 3);
        if (tileentity != null){
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    public static void setState(Direction facing, World worldIn, BlockPos pos){
        BlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, iblockstate.with(FACING, facing), 3);
        if (tileentity != null){
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack iStack) {
        super.onBlockPlacedBy(world, pos, state, placer, iStack);
        world.setBlockState(pos, state.with(FACING, canRotateVertical() ? Direction.getFacingDirections(placer)[0] : placer.getHorizontalFacing().getOpposite()), 2);
    }

}
