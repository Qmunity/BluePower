/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileEngine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author TheFjong, MoreThanHidden
 */
public class BlockEngine extends BlockContainerBase {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty GEAR = BooleanProperty.create("gear");
    public static final BooleanProperty GLIDER = BooleanProperty.create("glider");
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

    public BlockEngine() {

        super(Material.IRON, TileEngine.class);
        setDefaultState(this.stateContainer.getBaseState().with(ACTIVE, false).with(GEAR, false).with(GLIDER, false).with(FACING, Direction.DOWN));
        setRegistryName(Refs.MODID, Refs.ENGINE_NAME);
    }
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(ACTIVE, GEAR, GLIDER, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return Block.makeCuboidShape(0,0,0,16,9,16);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("cast")
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity player, ItemStack iStack) {
        if (world.getTileEntity(pos) instanceof TileEngine) {
            Direction facing;

            if (player.rotationPitch > 45) {

                facing = Direction.DOWN;
            } else if (player.rotationPitch < -45) {

                facing = Direction.UP;
            } else {

                facing = player.getHorizontalFacing().getOpposite();
            }

            TileEngine tile = (TileEngine) world.getTileEntity(pos);
            if (tile != null) {
                tile.setOrientation(facing);
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!player.inventory.getCurrentItem().isEmpty()) {
            Item item = player.inventory.getCurrentItem().getItem();
            if (item == BPItems.screwdriver) {
                return true;
            }
        }

        return super.onBlockActivated(blockState, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

}
